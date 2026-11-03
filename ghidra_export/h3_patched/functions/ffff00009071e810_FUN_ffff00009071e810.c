// ffff00009071e810  size=64  FUN_ffff00009071e810

void FUN_ffff00009071e810
               (code *UNRECOVERED_JUMPTABLE,undefined8 param_2,undefined8 param_3,undefined8 param_4
               ,undefined8 param_5)

{
  ulong uVar1;
  
  vbar_el1 = (ulong)PTR_DAT_ffff00009071e850 ^ 0xffff000000000000;
  InstructionSynchronizationBarrier();
  uVar1 = sctlr_el1;
  sctlr_el1 = uVar1 & 0xfffffffffffffffe;
  InstructionSynchronizationBarrier();
  TLBI_VMALLE1();
                    /* WARNING: Could not recover jumptable at 0xffff00009071e84c. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  (*UNRECOVERED_JUMPTABLE)
            (param_2,param_3,param_4,param_5,UNRECOVERED_JUMPTABLE,uVar1 & 0xfffffffffffffffe);
  return;
}


