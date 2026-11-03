// ffff000090748988  size=480  FUN_ffff000090748988

undefined8 FUN_ffff000090748988(undefined8 param_1)

{
  undefined4 uVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  undefined8 uVar6;
  long lVar7;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  
  iVar2 = FUN_ffff000090746fd0(param_1,s__soc_gps_ffff000090800f09,&DAT_ffff0000907d4922);
  if (iVar2 == -2) {
    FUN_ffff00009077e11c
              (s___s__Fail_to_get_dts_gps_node_ffff0000907de496,
               s_gps_post_allocation_ffff0000907dfcf4);
    return 0xfffffffe;
  }
  lVar3 = FUN_ffff0000907471c8(1);
  if (lVar3 == 0) {
    uVar6 = 1;
LAB_ffff000090748ac0:
    FUN_ffff00009077e11c
              (s___s__incorrect_region_idx___d__ffff0000907efea8,
               s_gps_post_allocation_ffff0000907dfcf4,uVar6);
    return 0xfffffffe;
  }
  lVar4 = FUN_ffff0000907471c8(7);
  if (lVar4 == 0) {
    uVar6 = 7;
    goto LAB_ffff000090748ac0;
  }
  lVar7 = *(long *)(lVar3 + 0x10);
  uStack_40 = *(undefined8 *)(lVar4 + 0x10);
  local_48 = 0;
  FUN_ffff00009077e11c
            (s___s__conn_pa___08lX___X__gps_pa__ffff0000907d957e,
             s_gps_post_allocation_ffff0000907dfcf4,lVar7,*(undefined4 *)(lVar3 + 0xc),uStack_40,
             *(undefined4 *)(lVar4 + 0xc));
  FUN_ffff0000907470a0
            (param_1,iVar2,&DAT_ffff0000907f6809,s_emi_addr_ffff0000907ff439,&uStack_40,&local_48);
  uVar1 = *(undefined4 *)(lVar3 + 0xc);
  local_38 = 0;
  if (lVar7 == 0) {
    pcVar5 = s___s__Invalid_addr_pa_ffff0000907ff49a;
  }
  else {
    iVar2 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,s_AP_RO_CONN_RO_ffff0000907e4de1,uVar1,&local_38,0xc,
                       lVar7,0,0);
    if (iVar2 == 0) {
      iVar2 = FUN_ffff000090785d18
                        (s_connsys_gnss_ffff0000907fbc20,s_connsys_gnss_ffff0000907fbc20,local_38,
                         uVar1);
      if (iVar2 < 0) {
        FUN_ffff00009077e11c
                  (s___s__CONN_RO_GPS_load_failed__re_ffff0000907e97e6,
                   s_load_fw_image_ffff0000907d95ab,iVar2);
      }
      else {
        FUN_ffff000090707c70(local_38,uVar1);
      }
      FUN_ffff00009077be30(DAT_ffff000090839928,local_38);
      goto LAB_ffff000090748b38;
    }
    pcVar5 = s___s__CONN_RO_vmm_alloc_failed_ffff0000907d64a7;
  }
  FUN_ffff00009077e11c(pcVar5,s_load_fw_image_ffff0000907d95ab);
LAB_ffff000090748b38:
  FUN_ffff00009077e11c(s___s__DONE_ffff0000907d9573,s_gps_post_allocation_ffff0000907dfcf4);
  return 0;
}


