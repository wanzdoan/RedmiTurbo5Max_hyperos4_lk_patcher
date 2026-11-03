
byte FUN_ffff0000b80061f0(uint param_1)

{
  int iVar1;
  undefined4 local_14;
  
  local_14 = 0;
  if (param_1 < 2) {
    iVar1 = FUN_ffff0000b802120c
                      (DAT_ffff0000b810c078,
                       *(undefined4 *)(&DAT_ffff0000b80a1fe8 + (ulong)param_1 * 0x18),&local_14);
    local_14._0_1_ = iVar1 == 0 & (byte)local_14;
  }
  else {
    FUN_ffff0000b804c33c
              (s__s__d__bat_idx__u__BUG__ffff0000b809c0f8,
               s_mt6379_is_imp_irq_triggered_ffff0000b8087d5f,0x5e);
    local_14._0_1_ = 0;
  }
  return (byte)local_14;
}


