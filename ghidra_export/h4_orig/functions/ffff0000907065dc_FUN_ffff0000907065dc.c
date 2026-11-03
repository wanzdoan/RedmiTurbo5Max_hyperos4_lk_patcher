// ffff0000907065dc  size=132  FUN_ffff0000907065dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

byte FUN_ffff0000907065dc(uint param_1)

{
  int iVar1;
  undefined4 local_14;
  
  local_14 = 0;
  if (param_1 < 2) {
    iVar1 = FUN_ffff000090737554
                      (_DAT_ffff00009083d088,
                       *(undefined4 *)(&DAT_ffff000090803da8 + (ulong)param_1 * 0x18),&local_14);
    local_14._0_1_ = iVar1 == 0 & (byte)local_14;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__bat_idx__u__BUG__ffff0000907f9207,
               s_mt6379_is_imp_irq_triggered_ffff0000907d3d45,0x5e);
    local_14._0_1_ = 0;
  }
  return (byte)local_14;
}


