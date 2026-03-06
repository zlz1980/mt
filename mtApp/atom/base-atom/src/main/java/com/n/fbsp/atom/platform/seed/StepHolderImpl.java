package com.n.fbsp.atom.platform.seed;

import com.nantian.nbp.base.model.FlowUnit;
import com.nantian.nbp.flow.engine.service.api.Atom;
import com.nantian.nbp.flow.engine.service.api.AtomService;
import com.nantian.nbp.flow.engine.service.api.ScopeValUnit;
import com.nantian.nbp.flow.engine.service.api.TranContext;
import com.nantian.nbp.flow.engine.service.api.context.AtomResult;
import com.nantian.nbp.flow.engine.service.api.context.RetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Atom("base.StepHolder")
public class StepHolderImpl implements AtomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepHolderImpl.class);

    @Override
    public AtomResult doService(TranContext tranContext, ScopeValUnit scopeValUnit, FlowUnit flowUnit) {
        LOGGER.info("Current step is stepHolder.");
        return new AtomResult(RetType.SUCCESS);
    }
}
