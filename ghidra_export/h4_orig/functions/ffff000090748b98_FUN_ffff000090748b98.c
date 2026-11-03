// ffff000090748b98  size=700  FUN_ffff000090748b98

undefined4 FUN_ffff000090748b98(undefined8 param_1)

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
  
  FUN_ffff0000907471b4();
  iVar1 = FUN_ffff000090746fd0(param_1,s__soc_connscp_ffff0000907e4def,s__connscp_ffff0000907fbc2d);
  if (iVar1 == -2) {
    pcVar5 = s___s__Fail_to_get_dts_connscp_nod_ffff0000907f680d;
LAB_ffff000090748d6c:
    FUN_ffff00009077e11c(pcVar5,s_setup_scp_region_ffff0000907ec986);
  }
  else {
    iVar2 = FUN_ffff000090747044(param_1,iVar1,s_report_region_idx_ffff0000907ec997);
    if (iVar2 == 0) {
      pcVar5 = s___s__report_region_idx_not_found_ffff0000907e659a;
      goto LAB_ffff000090748d6c;
    }
    lVar4 = FUN_ffff0000907471c8();
    if (lVar4 == 0) {
      pcVar5 = s___s__report_region_not_found_ffff0000907fbc53;
      goto LAB_ffff000090748d6c;
    }
    local_40 = 0;
    FUN_ffff00009077e11c
              (s___s__report_region___d____lx___x_ffff0000907d4927,
               s_setup_scp_region_ffff0000907ec986,iVar2,*(undefined8 *)(lVar4 + 0x10),
               *(undefined4 *)(lVar4 + 0xc));
    local_38 = *(undefined8 *)(lVar4 + 0x10);
    uVar6 = *(undefined4 *)(lVar4 + 0xc);
    iVar2 = FUN_ffff0000907470a0
                      (param_1,iVar1,s_report_buf_addr_ffff0000907e4e15,
                       s_report_buf_addr_ffff0000907d64c6,&local_38,&local_40);
    if (iVar2 != 0) {
      FUN_ffff00009077e11c
                (s___s__set_fdt_fail_ret___d__ffff0000907f4ce9,s_setup_scp_region_ffff0000907ec986,
                 iVar2);
    }
    iVar2 = FUN_ffff000090747118
                      (param_1,iVar1,s_report_buf_size_ffff0000907dcb19,
                       s_report_buf_size_ffff0000907f6830,uVar6);
    if (iVar2 != 0) {
      FUN_ffff00009077e11c
                (s___s__set_fdt_fail_ret___d__ffff0000907f4ce9,s_setup_scp_region_ffff0000907ec986,
                 iVar2);
    }
    iVar1 = FUN_ffff000090747044(param_1,iVar1,s_batch_region_idx_ffff0000907efef8);
    if (iVar1 == 0) {
      pcVar5 = s___s__batch_region_idx_not_found_ffff0000907dcb29;
      goto LAB_ffff000090748d6c;
    }
    lVar3 = FUN_ffff0000907471c8();
    if (lVar3 == 0) {
      pcVar5 = s___s__batch_region_not_found_ffff000090800f12;
      goto LAB_ffff000090748d6c;
    }
    FUN_ffff00009077e11c
              (s___s__report___lx__batching___lx__ffff0000907fbc71,
               s_setup_scp_region_ffff0000907ec986,*(undefined8 *)(lVar4 + 0x10),
               *(undefined8 *)(lVar3 + 0x10),*(undefined4 *)(lVar3 + 0xc));
    uVar7 = *(undefined8 *)(lVar4 + 0x10);
    iVar1 = *(int *)(lVar3 + 0xc) + (*(int *)(lVar3 + 0x10) - (int)uVar7);
    FUN_ffff00009077e11c
              (s___s__remapaddr___lx__size___x__ffff0000907e65bc,s_setup_scp_region_ffff0000907ec986
               ,uVar7,iVar1);
    FUN_ffff000090738b48(3,uVar7,iVar1);
  }
  iVar1 = FUN_ffff000090746fd0(param_1,s__soc_connscp_ffff0000907e4def,s__connscp_ffff0000907fbc2d);
  if (iVar1 == -2) {
    uVar6 = 0xfffffffe;
    pcVar5 = s___s__Fail_to_get_dts_connscp_nod_ffff0000907f680d;
  }
  else {
    iVar1 = FUN_ffff000090747044(param_1,iVar1,s_dfd_region_idx_ffff0000907db111);
    if (iVar1 == 0) {
      FUN_ffff00009077e11c
                (s___s__DFD_not_support_ffff0000907db120,s_conn_scp_post_allocation_ffff0000907e4dfc
                );
    }
    else {
      lVar4 = FUN_ffff0000907471c8();
      if (lVar4 == 0) {
        uVar6 = 0xfffffffe;
        pcVar5 = s___s__Fail_to_get_dfd_region_ffff0000907fbc36;
        goto LAB_ffff000090748e2c;
      }
      FUN_ffff00009077e11c
                (s___s__dfd_pa___lx___x__ffff0000907e7d5c,
                 s_conn_scp_post_allocation_ffff0000907e4dfc,*(undefined8 *)(lVar4 + 0x10),
                 *(undefined4 *)(lVar4 + 0xc));
      FUN_ffff000090748e54(param_1,*(undefined8 *)(lVar4 + 0x10),*(undefined4 *)(lVar4 + 0xc));
    }
    uVar6 = 0;
    pcVar5 = s___s__DONE_ffff0000907d9573;
  }
LAB_ffff000090748e2c:
  FUN_ffff00009077e11c(pcVar5,s_conn_scp_post_allocation_ffff0000907e4dfc);
  return uVar6;
}


