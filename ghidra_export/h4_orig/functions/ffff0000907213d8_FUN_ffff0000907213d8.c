// ffff0000907213d8  size=184  FUN_ffff0000907213d8

int FUN_ffff0000907213d8(long param_1,int param_2)

{
  int iVar1;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  iVar1 = -1;
  if ((param_1 != 0) && (4 < param_2)) {
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    iVar1 = FUN_ffff000090721098(0x4a,&local_50,0x20);
    if (iVar1 == 0) {
      FUN_ffff00009077f2ac(param_1,(ulong)&local_50 | 5,4);
      *(undefined1 *)(param_1 + 4) = 0;
      FUN_ffff00009077e11c
                (s__XMCHG_LK_BQ28Z610___s__battery_m_ffff0000907e0ef3,
                 s_bq28z610_get_battery_model_id_ffff0000908022ff,param_1);
    }
    else {
      FUN_ffff00009077e11c
                (s__XMCHG_LK_BQ28Z610___s__failed_t_ffff0000907d5cbe,
                 s_bq28z610_get_battery_model_id_ffff0000908022ff,iVar1);
    }
  }
  return iVar1;
}


