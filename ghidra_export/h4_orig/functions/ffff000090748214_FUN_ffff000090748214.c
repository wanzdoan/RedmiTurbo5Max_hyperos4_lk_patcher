// ffff000090748214  size=300  FUN_ffff000090748214

void FUN_ffff000090748214(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  undefined4 uVar3;
  int iVar4;
  
  iVar1 = FUN_ffff000090746fd0(param_1,s__soc_connv3_ffff0000907fa04d,s__connv3_ffff0000907f2fd7);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c(s_connv3_node_not_found_ffff0000907e7d45);
    return;
  }
  iVar2 = FUN_ffff000090747044(param_1,iVar1,s_co_clock_ffff0000907e97dd);
  uVar3 = FUN_ffff000090747044(param_1,iVar1,s_ext_32k_ticks_ffff0000907d1566);
  iVar4 = FUN_ffff000090785c48(0,8);
  FUN_ffff00009077e11c
            (s___s__is_coclock__d_ext_32k_ticks_ffff0000907fd897,
             s_consys_sku_detection_ffff0000907d2fdd,iVar2,uVar3,iVar4);
  if ((iVar2 == 1) && (iVar4 == 1)) {
    FUN_ffff000090747118(param_1,iVar1,s_co_clock_ffff0000907e97dd,s_co_clock_ffff0000907e97dd,0);
    FUN_ffff000090747118
              (param_1,iVar1,s_ext_32k_ticks_ffff0000907d1566,s_ext_32k_ticks_ffff0000907d1566,
               0x7ef4);
    return;
  }
  return;
}


