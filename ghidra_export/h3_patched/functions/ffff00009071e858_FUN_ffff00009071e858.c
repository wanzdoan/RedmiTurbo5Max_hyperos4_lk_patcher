// ffff00009071e858  size=156  FUN_ffff00009071e858

void FUN_ffff00009071e858(long param_1,undefined8 param_2,undefined8 param_3,long param_4)

{
  undefined8 uVar1;
  ulong in_x9;
  ulong in_x10;
  
  if (param_4 != 0) {
    do {
      if (1 < (in_x9 >> (in_x10 + (in_x10 >> 1) & 0x3f) & 7)) {
        csselr_el1 = in_x10;
        InstructionSynchronizationBarrier();
        uVar1 = ccsidr_el1;
        UnkSytemRegRead(3,0,0,7,2);
        DataSynchronizationBarrier(3,3,0);
                    /* WARNING: Could not recover jumptable at 0xffff00009071e8d4. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
        (*(code *)(param_1 * 0x24 + -0xffff6f8e170c))(1);
        return;
      }
      in_x10 = in_x10 + 2;
    } while ((long)in_x10 < param_4);
    csselr_el1 = 0;
    DataSynchronizationBarrier(3,3,0);
    InstructionSynchronizationBarrier();
  }
  return;
}


