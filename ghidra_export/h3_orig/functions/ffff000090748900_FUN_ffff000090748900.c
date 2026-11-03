// ffff000090748900  size=700  FUN_ffff000090748900

undefined4 FUN_ffff000090748900(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  undefined4 uVar6;
  undefined8 uVar7;
  undefined8 local_40;
  undefined8 local_38;
  
  FUN_ffff000090746f1c();
  iVar1 = FUN_ffff000090746d38(param_1,s__soc_connscp_ffff0000907e4ceb,s__connscp_ffff0000907fba91);
  if (iVar1 == -2) {
    pcVar5 = s___s__Fail_to_get_dts_connscp_nod_ffff0000907f6683;
LAB_ffff000090748ad4:
    FUN_ffff00009077de54(pcVar5,s_setup_scp_region_ffff0000907ec803);
  }
  else {
    iVar2 = FUN_ffff000090746dac(param_1,iVar1,s_report_region_idx_ffff0000907ec814);
    if (iVar2 == 0) {
      pcVar5 = s___s__report_region_idx_not_found_ffff0000907e6486;
      goto LAB_ffff000090748ad4;
    }
    lVar4 = FUN_ffff000090746f30();
    if (lVar4 == 0) {
      pcVar5 = s___s__report_region_not_found_ffff0000907fbab7;
      goto LAB_ffff000090748ad4;
    }
    local_40 = 0;
    FUN_ffff00009077de54
              (s___s__report_region___d____lx___x_ffff0000907d48ea,
               s_setup_scp_region_ffff0000907ec803,iVar2,*(undefined8 *)(lVar4 + 0x10),
               *(undefined4 *)(lVar4 + 0xc));
    local_38 = *(undefined8 *)(lVar4 + 0x10);
    uVar6 = *(undefined4 *)(lVar4 + 0xc);
    iVar2 = FUN_ffff000090746e08
                      (param_1,iVar1,s_report_buf_addr_ffff0000907e4d11,
                       s_report_buf_addr_ffff0000907d6430,&local_38,&local_40);
    if (iVar2 != 0) {
      FUN_ffff00009077de54
                (s___s__set_fdt_fail_ret___d__ffff0000907f4b7c,s_setup_scp_region_ffff0000907ec803,
                 iVar2);
    }
    iVar2 = FUN_ffff000090746e80
                      (param_1,iVar1,s_report_buf_size_ffff0000907dca43,
                       s_report_buf_size_ffff0000907f66a6,uVar6);
    if (iVar2 != 0) {
      FUN_ffff00009077de54
                (s___s__set_fdt_fail_ret___d__ffff0000907f4b7c,s_setup_scp_region_ffff0000907ec803,
                 iVar2);
    }
    iVar1 = FUN_ffff000090746dac(param_1,iVar1,s_batch_region_idx_ffff0000907efd87);
    if (iVar1 == 0) {
      pcVar5 = s___s__batch_region_idx_not_found_ffff0000907dca53;
      goto LAB_ffff000090748ad4;
    }
    lVar3 = FUN_ffff000090746f30();
    if (lVar3 == 0) {
      pcVar5 = s___s__batch_region_not_found_ffff000090800d36;
      goto LAB_ffff000090748ad4;
    }
    FUN_ffff00009077de54
              (s___s__report___lx__batching___lx__ffff0000907fbad5,
               s_setup_scp_region_ffff0000907ec803,*(undefined8 *)(lVar4 + 0x10),
               *(undefined8 *)(lVar3 + 0x10),*(undefined4 *)(lVar3 + 0xc));
    uVar7 = *(undefined8 *)(lVar4 + 0x10);
    iVar1 = *(int *)(lVar3 + 0xc) + (*(int *)(lVar3 + 0x10) - (int)uVar7);
    FUN_ffff00009077de54
              (s___s__remapaddr___lx__size___x__ffff0000907e64a8,s_setup_scp_region_ffff0000907ec803
               ,uVar7,iVar1);
    FUN_ffff0000907388b0(3,uVar7,iVar1);
  }
  iVar1 = FUN_ffff000090746d38(param_1,s__soc_connscp_ffff0000907e4ceb,s__connscp_ffff0000907fba91);
  if (iVar1 == -2) {
    uVar6 = 0xfffffffe;
    pcVar5 = s___s__Fail_to_get_dts_connscp_nod_ffff0000907f6683;
  }
  else {
    iVar1 = FUN_ffff000090746dac(param_1,iVar1,s_dfd_region_idx_ffff0000907db006);
    if (iVar1 == 0) {
      FUN_ffff00009077de54
                (s___s__DFD_not_support_ffff0000907db015,s_conn_scp_post_allocation_ffff0000907e4cf8
                );
    }
    else {
      lVar4 = FUN_ffff000090746f30();
      if (lVar4 == 0) {
        uVar6 = 0xfffffffe;
        pcVar5 = s___s__Fail_to_get_dfd_region_ffff0000907fba9a;
        goto LAB_ffff000090748b94;
      }
      FUN_ffff00009077de54
                (s___s__dfd_pa___lx___x__ffff0000907e7c18,
                 s_conn_scp_post_allocation_ffff0000907e4cf8,*(undefined8 *)(lVar4 + 0x10),
                 *(undefined4 *)(lVar4 + 0xc));
      FUN_ffff000090748bbc(param_1,*(undefined8 *)(lVar4 + 0x10),*(undefined4 *)(lVar4 + 0xc));
    }
    uVar6 = 0;
    pcVar5 = s___s__DONE_ffff0000907d94dc;
  }
LAB_ffff000090748b94:
  FUN_ffff00009077de54(pcVar5,s_conn_scp_post_allocation_ffff0000907e4cf8);
  return uVar6;
}


