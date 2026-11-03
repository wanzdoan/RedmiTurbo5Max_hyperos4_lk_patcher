// ffff000090721350  size=136  FUN_ffff000090721350

undefined1 FUN_ffff000090721350(void)

{
  undefined1 uVar1;
  int iVar2;
  undefined8 local_30;
  undefined8 uStack_28;
  undefined8 local_20;
  undefined8 uStack_18;
  
  local_20 = 0;
  uStack_18 = 0;
  local_30 = 0;
  uStack_28 = 0;
  iVar2 = FUN_ffff000090721098(0x4b,&local_30,0x20);
  if (iVar2 == 0) {
    FUN_ffff00009077e11c
              (s__XMCHG_LK_BQ28Z610___s__FG_MAC_C_ffff0000907fb4b6,
               s_bq28z610_get_battery_cell_suppli_ffff0000908007be,&local_30);
  }
  else {
    FUN_ffff00009077e11c
              (s__XMCHG_LK_BQ28Z610___s__failed_t_ffff0000907d8fcc,
               s_bq28z610_get_battery_cell_suppli_ffff0000908007be,iVar2);
  }
  uVar1 = 2;
  if (local_30._1_1_ != 'J') {
    uVar1 = local_30._1_1_ == 'L';
  }
  return uVar1;
}


