// ffff000090747f7c  size=300  FUN_ffff000090747f7c

void FUN_ffff000090747f7c(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  undefined4 uVar3;
  int iVar4;
  
  iVar1 = FUN_ffff000090746d38(param_1,s__soc_connv3_ffff0000907f9eb1,s__connv3_ffff0000907f2e34);
  if (iVar1 < 0) {
    FUN_ffff00009077de54(s_connv3_node_not_found_ffff0000907e7bea);
    return;
  }
  iVar2 = FUN_ffff000090746dac(param_1,iVar1,s_co_clock_ffff0000907e9682);
  uVar3 = FUN_ffff000090746dac(param_1,iVar1,s_ext_32k_ticks_ffff0000907d1529);
  iVar4 = FUN_ffff000090785980(0,8);
  FUN_ffff00009077de54
            (s___s__is_coclock__d_ext_32k_ticks_ffff0000907fd6ef,
             s_consys_sku_detection_ffff0000907d2fa0,iVar2,uVar3,iVar4);
  if ((iVar2 == 1) && (iVar4 == 1)) {
    FUN_ffff000090746e80(param_1,iVar1,s_co_clock_ffff0000907e9682,s_co_clock_ffff0000907e9682,0);
    FUN_ffff000090746e80
              (param_1,iVar1,s_ext_32k_ticks_ffff0000907d1529,s_ext_32k_ticks_ffff0000907d1529,
               0x7ef4);
    return;
  }
  return;
}


