// ffff000090706680  size=132  FUN_ffff000090706680

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

byte FUN_ffff000090706680(uint param_1)

{
  int iVar1;
  undefined4 local_14;
  
  local_14 = 0;
  if (param_1 < 2) {
    iVar1 = FUN_ffff0000907372bc
                      (_DAT_ffff00009083d0e8,
                       *(undefined4 *)(&DAT_ffff000090803b80 + (ulong)param_1 * 0x18),&local_14);
    local_14._0_1_ = iVar1 == 0 & (byte)local_14;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__bat_idx__u__BUG__ffff0000907f906b,
               s_mt6379_is_imp_irq_triggered_ffff0000907d3d08,0x5e);
    local_14._0_1_ = 0;
  }
  return (byte)local_14;
}


