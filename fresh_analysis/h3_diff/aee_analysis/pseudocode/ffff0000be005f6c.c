
byte FUN_ffff0000be005f6c(uint param_1)

{
  int iVar1;
  undefined4 local_14;
  
  local_14 = 0;
  if (param_1 < 2) {
    iVar1 = FUN_ffff0000be029958
                      (DAT_ffff0000be0f9058,
                       *(undefined4 *)(&DAT_ffff0000be0c7560 + (ulong)param_1 * 0x18),&local_14);
    local_14._0_1_ = iVar1 == 0 & (byte)local_14;
  }
  else {
    FUN_ffff0000be05fd98
              (s__s__d__bat_idx__u__BUG__ffff0000be0c02bb,
               s_mt6379_is_imp_irq_triggered_ffff0000be0a67bc,0x5e);
    local_14._0_1_ = 0;
  }
  return (byte)local_14;
}


