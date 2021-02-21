/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Trustly Group AB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bambora.trustlyintegration.trustly.requestbuilders;

import com.bambora.trustlyintegration.trustly.commons.Method;
import com.bambora.trustlyintegration.trustly.data.request.Request;
import com.bambora.trustlyintegration.trustly.data.request.RequestParameters;
import com.bambora.trustlyintegration.trustly.data.request.requestdata.ApproveWithdrawalData;
import com.bambora.trustlyintegration.trustly.security.SignatureHandler;


/**
 * Creates a ApproveWithdrawal request ready to be sent to Trustly API.
 * The constructor contains the required fields of a ApproveWithdrawal request
 *
 * Builder lets you add additional information if any is available for the given request.
 *
 * The API specifics of the request can be found on https://trustly.com/en/developer/
 *
 * Example use for a default ApproveWithdrawal request:
 * Request approveWithdrawal = new ApproveWithdrawal.Build(orderid).getRequest();
 */
public class ApproveWithdrawal {
    private final Request request = new Request();

    private ApproveWithdrawal(final Build builder) {
        final RequestParameters params = new RequestParameters();
        params.setUUID(SignatureHandler.generateNewUUID());
        params.setData(builder.data);

        request.setMethod(Method.APPROVE_WITHDRAWAL);
        request.setParams(params);
    }

    public Request getRequest() {
        return request;
    }

    public static class Build {
        private final ApproveWithdrawalData data = new ApproveWithdrawalData();

        public Build(final String orderID) {
            data.setOrderID(orderID);
        }

        public Request getRequest() {
            return new ApproveWithdrawal(this).getRequest();
        }
    }
}
