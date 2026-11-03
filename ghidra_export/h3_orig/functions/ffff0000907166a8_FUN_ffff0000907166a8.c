// ffff0000907166a8  size=172  FUN_ffff0000907166a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907166a8(undefined8 param_1,long param_2,ulong param_3)

{
  ulong uVar1;
  long lVar2;
  ulong uVar3;
  
  if (param_2 == 0) {
    uVar3 = (_DAT_ffff000090843648 >> 0x20) * (param_3 & 0xffffffff);
    uVar1 = ((ulong)_DAT_ffff000090843650 * (param_3 & 0xffffffff) >> 0x20) + (uVar3 & 0xffffffff);
    uVar1 = (uVar3 >> 0x20) + (_DAT_ffff000090843648 & 0xffffffff) * (param_3 & 0xffffffff) +
            (uVar1 >> 0x20) + (uVar1 >> 0x1f & 1);
    _DAT_ffff000090843638 = param_1;
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
  FUN_ffff00009077cd04
            (s_dev_timer_arm_generic_arm_generi_ffff0000907fe996,0xb5,s_arg____NULL_ffff0000907fb18c
            );
}


