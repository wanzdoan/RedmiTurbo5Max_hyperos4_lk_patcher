// ffff0000907486f0  size=480  FUN_ffff0000907486f0

undefined8 FUN_ffff0000907486f0(undefined8 param_1)

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
  
  iVar2 = FUN_ffff000090746d38(param_1,s__soc_gps_ffff000090800d2d,&DAT_ffff0000907d48e5);
  if (iVar2 == -2) {
    FUN_ffff00009077de54
              (s___s__Fail_to_get_dts_gps_node_ffff0000907de3dc,
               s_gps_post_allocation_ffff0000907dfc3a);
    return 0xfffffffe;
  }
  lVar3 = FUN_ffff000090746f30(1);
  if (lVar3 == 0) {
    uVar6 = 1;
LAB_ffff000090748828:
    FUN_ffff00009077de54
              (s___s__incorrect_region_idx___d__ffff0000907efd20,
               s_gps_post_allocation_ffff0000907dfc3a,uVar6);
    return 0xfffffffe;
  }
  lVar4 = FUN_ffff000090746f30(7);
  if (lVar4 == 0) {
    uVar6 = 7;
    goto LAB_ffff000090748828;
  }
  lVar7 = *(long *)(lVar3 + 0x10);
  uStack_40 = *(undefined8 *)(lVar4 + 0x10);
  local_48 = 0;
  FUN_ffff00009077de54
            (s___s__conn_pa___08lX___X__gps_pa__ffff0000907d94e7,
             s_gps_post_allocation_ffff0000907dfc3a,lVar7,*(undefined4 *)(lVar3 + 0xc),uStack_40,
             *(undefined4 *)(lVar4 + 0xc));
  FUN_ffff000090746e08
            (param_1,iVar2,&DAT_ffff0000907f6668,s_emi_addr_ffff0000907ff291,&uStack_40,&local_48);
  uVar1 = *(undefined4 *)(lVar3 + 0xc);
  local_38 = 0;
  if (lVar7 == 0) {
    pcVar5 = s___s__Invalid_addr_pa_ffff0000907ff2f2;
  }
  else {
    iVar2 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_AP_RO_CONN_RO_ffff0000907e4cc6,uVar1,&local_38,0xc,
                       lVar7,0,0);
    if (iVar2 == 0) {
      iVar2 = FUN_ffff000090785a50
                        (s_connsys_gnss_ffff0000907fba84,s_connsys_gnss_ffff0000907fba84,local_38,
                         uVar1);
      if (iVar2 < 0) {
        FUN_ffff00009077de54
                  (s___s__CONN_RO_GPS_load_failed__re_ffff0000907e968b,
                   s_load_fw_image_ffff0000907d9514,iVar2);
      }
      else {
        FUN_ffff000090707ce4(local_38,uVar1);
      }
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_38);
      goto LAB_ffff0000907488a0;
    }
    pcVar5 = s___s__CONN_RO_vmm_alloc_failed_ffff0000907d6411;
  }
  FUN_ffff00009077de54(pcVar5,s_load_fw_image_ffff0000907d9514);
LAB_ffff0000907488a0:
  FUN_ffff00009077de54(s___s__DONE_ffff0000907d94dc,s_gps_post_allocation_ffff0000907dfc3a);
  return 0;
}


