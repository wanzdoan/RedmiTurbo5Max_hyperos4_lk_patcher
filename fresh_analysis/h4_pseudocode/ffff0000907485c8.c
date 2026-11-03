
undefined8 FUN_ffff0000907485c8(undefined8 param_1)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  long lVar6;
  long lVar7;
  char *pcVar8;
  undefined8 local_58;
  
  iVar1 = FUN_ffff000090746fd0(param_1,s__soc_wifi_ffff0000907fd8c4,s__wifi_ffff0000907e4dbc);
  if (iVar1 < 0) {
    pcVar8 = s___s___soc_wifi_node_not_found_ffff0000907e4dc2;
  }
  else {
    uVar2 = FUN_ffff000090747044(param_1,iVar1,s_emi_alignment_ffff0000907fd860);
    lVar3 = FUN_ffff0000907471c8(0);
    if (lVar3 == 0) {
      FUN_ffff00009077e11c
                (s___s__incorrect_region_idx___d__ffff0000907efea8,
                 s_wifi_post_allocation_ffff0000907f17c8,0);
      return 0xfffffffe;
    }
    local_58 = 0;
    FUN_ffff00009077e11c
              (s___s__wifi_pa___lX__alignment___x_ffff0000907f67e6,
               s_wifi_post_allocation_ffff0000907f17c8,*(undefined8 *)(lVar3 + 0x10),uVar2);
    FUN_ffff0000907470a0
              (param_1,iVar1,s_wifi_emi_ffff0000907de45b,s_conninfra_emi_addr_ffff0000907eb10f,
               (undefined8 *)(lVar3 + 0x10),&local_58);
    iVar1 = DAT_ffff0000908776f8;
    lVar3 = 0;
    if ((DAT_ffff00009081a748 == 0) && (DAT_ffff0000908776f8 != 0)) {
      uVar4 = FUN_ffff0000907471a0();
      uVar5 = FUN_ffff00009074718c();
      lVar3 = FUN_ffff00009071a09c
                        (iVar1,uVar2,uVar4,uVar5,0,2,
                         s_shared_dma_pool_wifi_page_pool_r_ffff0000907de464);
      if (lVar3 == 0) {
        pcVar8 = s___s__page_pool_mblock_pa_allocat_ffff0000907cfa4d;
        goto LAB_ffff000090748944;
      }
    }
    FUN_ffff00009077e11c
              (s___s__page_pool_pa___lX__ffff0000907efed1,s_wifi_post_allocation_ffff0000907f17c8,
               lVar3);
    lVar6 = FUN_ffff00009071b0d8();
    lVar7 = FUN_ffff00009071b008();
    FUN_ffff00009077e11c
              (s___s__dram_start__0x_llX___size___ffff0000907ff442,
               s_wifi_post_allocation_ffff0000907f17c8,lVar6,lVar7);
    iVar1 = DAT_ffff0000908776fc;
    if (((ulong)(lVar7 + lVar6) < 0x400000001) || (DAT_ffff0000908776fc == 0)) {
      FUN_ffff00009077e11c
                (s___s__dram_addr_range_<_16GB_ffff0000908029d6,
                 s_wifi_post_allocation_ffff0000907f17c8);
      lVar6 = 0;
    }
    else {
      uVar4 = FUN_ffff0000907471a0();
      uVar5 = FUN_ffff00009074718c();
      lVar6 = FUN_ffff00009071a09c
                        (iVar1,uVar2,uVar4,uVar5,0,2,
                         s_shared_dma_pool_wifi_tx_cma_rese_ffff000090800eda);
      if (lVar6 == 0) {
        pcVar8 = s___s__tx_cma_pa_allocation_failed_ffff0000907ff469;
LAB_ffff000090748944:
        FUN_ffff00009077e11c(pcVar8,s_wifi_post_allocation_ffff0000907f17c8);
        return 0xfffffffb;
      }
    }
    FUN_ffff00009077e11c
              (s___s__tx_cma_pa___lX__ffff0000907d7d43,s_wifi_post_allocation_ffff0000907f17c8,lVar6
              );
    iVar1 = FUN_ffff00009076da24(param_1,s__soc_wifi_misc_ffff0000907ff48b);
    pcVar8 = s__soc_wifi_misc_ffff0000907ff48b;
    if (iVar1 < 0) {
      pcVar8 = s__wifi_misc_ffff000090802a20;
    }
    iVar1 = FUN_ffff00009071a14c(pcVar8,s_shared_dma_pool_wifi_misc_reserv_ffff0000908029f3);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c
                (s__s_wifi_misc_pre_alloc_phandle_f_ffff0000907f17dd,
                 s_wifi_post_allocation_ffff0000907f17c8,iVar1);
    }
    iVar1 = FUN_ffff00009076da24(param_1,s__soc_wifi_ffff0000907fd8c4);
    pcVar8 = s__soc_wifi_ffff0000907fd8c4;
    if (iVar1 < 0) {
      pcVar8 = s__wifi_ffff0000907e4dbc;
    }
    iVar1 = FUN_ffff00009071a14c(pcVar8,s_shared_dma_pool_wifi_reserve_mem_ffff0000907fbbf8);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c
                (s__s_pre_alloc_phandle_failed___re_ffff0000907eb122,
                 s_wifi_post_allocation_ffff0000907f17c8,iVar1);
    }
    if (lVar3 != 0) {
      iVar1 = FUN_ffff00009076da24(param_1,s__soc_wifi_page_pool_ffff0000907fbbe4);
      pcVar8 = s__soc_wifi_page_pool_ffff0000907fbbe4;
      if (iVar1 < 0) {
        pcVar8 = s__wifi_page_pool_ffff0000907f67d6;
      }
      iVar1 = FUN_ffff00009071a14c(pcVar8,s_shared_dma_pool_wifi_page_pool_r_ffff0000907de464);
      if (iVar1 != 0) {
        FUN_ffff00009077e11c
                  (s__s_pre_alloc_phandle_failed___re_ffff0000907eb122,
                   s_wifi_post_allocation_ffff0000907f17c8,iVar1);
      }
    }
    if (lVar6 != 0) {
      iVar1 = FUN_ffff00009076da24(param_1,s__soc_wifi_tx_cma_ffff0000907d6496);
      pcVar8 = s__soc_wifi_tx_cma_ffff0000907d6496;
      if (iVar1 < 0) {
        pcVar8 = s__wifi_tx_cma_ffff0000907f2ff9;
      }
      iVar1 = FUN_ffff00009071a14c(pcVar8,s_shared_dma_pool_wifi_tx_cma_rese_ffff000090800eda);
      if (iVar1 != 0) {
        FUN_ffff00009077e11c
                  (s__s_pre_alloc_phandle_failed___re_ffff0000907eb122,
                   s_wifi_post_allocation_ffff0000907f17c8,iVar1);
      }
    }
    pcVar8 = s___s__DONE_ffff0000907d9573;
  }
  FUN_ffff00009077e11c(pcVar8,s_wifi_post_allocation_ffff0000907f17c8);
  return 0;
}


