
undefined8 FUN_ffff0000be0197e4(undefined8 param_1,long param_2,ulong param_3)

{
  ulong uVar1;
  long lVar2;
  ulong uVar3;
  
  if (param_2 == 0) {
    uVar3 = (DAT_ffff0000be10c6b8 >> 0x20) * (param_3 & 0xffffffff);
    uVar1 = ((ulong)DAT_ffff0000be10c6c0 * (param_3 & 0xffffffff) >> 0x20) + (uVar3 & 0xffffffff);
    uVar1 = (uVar3 >> 0x20) + (DAT_ffff0000be10c6b8 & 0xffffffff) * (param_3 & 0xffffffff) +
            (uVar1 >> 0x20) + (uVar1 >> 0x1f & 1);
    DAT_ffff0000be10c6a8 = param_1;
    if (uVar1 >> 0x1f == 0) {
      cntp_tval_el0 = uVar1;
      InstructionSynchronizationBarrier();
    }
    else {
      lVar2 = cntpct_el0;
      cntp_cval_el0 = lVar2 + uVar1;
      InstructionSynchronizationBarrier();
    }
    cntp_ctl_el0 = 1;
    InstructionSynchronizationBarrier();
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_dev_timer_arm_generic_arm_generi_ffff0000be0c3dd3,0xb5,s_arg____NULL_ffff0000be0c194f
            );
}


