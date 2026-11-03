// ffff000090748330  size=912  FUN_ffff000090748330

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090748330(undefined8 param_1)

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
  
  iVar1 = FUN_ffff000090746d38(param_1,s__soc_wifi_ffff0000907fd71c,s__wifi_ffff0000907e4ca1);
  if (iVar1 < 0) {
    pcVar8 = s___s___soc_wifi_node_not_found_ffff0000907e4ca7;
  }
  else {
    uVar2 = FUN_ffff000090746dac(param_1,iVar1,s_emi_alignment_ffff0000907fd6b8);
    lVar3 = FUN_ffff000090746f30(0);
    if (lVar3 == 0) {
      FUN_ffff00009077de54
                (s___s__incorrect_region_idx___d__ffff0000907efd20,
                 s_wifi_post_allocation_ffff0000907f1640,0);
      return 0xfffffffe;
    }
    local_58 = 0;
    FUN_ffff00009077de54
              (s___s__wifi_pa___lX__alignment___x_ffff0000907f6645,
               s_wifi_post_allocation_ffff0000907f1640,*(undefined8 *)(lVar3 + 0x10),uVar2);
    FUN_ffff000090746e08
              (param_1,iVar1,s_wifi_emi_ffff0000907de3a1,s_conninfra_emi_addr_ffff0000907eaf75,
               (undefined8 *)(lVar3 + 0x10),&local_58);
    iVar1 = _DAT_ffff000090877660;
    lVar3 = 0;
    if ((DAT_ffff00009081a728 == 0) && (_DAT_ffff000090877660 != 0)) {
      uVar4 = FUN_ffff000090746f08();
      uVar5 = FUN_ffff000090746ef4();
      lVar3 = FUN_ffff000090719f58
                        (iVar1,uVar2,uVar4,uVar5,0,2,
                         s_shared_dma_pool_wifi_page_pool_r_ffff0000907de3aa);
      if (lVar3 == 0) {
        pcVar8 = s___s__page_pool_mblock_pa_allocat_ffff0000907cfa2e;
        goto LAB_ffff0000907486ac;
      }
    }
    FUN_ffff00009077de54
              (s___s__page_pool_pa___lX__ffff0000907efd49,s_wifi_post_allocation_ffff0000907f1640,
               lVar3);
    lVar6 = FUN_ffff00009071af94();
    lVar7 = FUN_ffff00009071aec4();
    FUN_ffff00009077de54
              (s___s__dram_start__0x_llX___size___ffff0000907ff29a,
               s_wifi_post_allocation_ffff0000907f1640,lVar6,lVar7);
    iVar1 = _DAT_ffff000090877664;
    if (((ulong)(lVar7 + lVar6) < 0x400000001) || (_DAT_ffff000090877664 == 0)) {
      FUN_ffff00009077de54
                (s___s__dram_addr_range_<_16GB_ffff0000908027d2,
                 s_wifi_post_allocation_ffff0000907f1640);
      lVar6 = 0;
    }
    else {
      uVar4 = FUN_ffff000090746f08();
      uVar5 = FUN_ffff000090746ef4();
      lVar6 = FUN_ffff000090719f58
                        (iVar1,uVar2,uVar4,uVar5,0,2,
                         s_shared_dma_pool_wifi_tx_cma_rese_ffff000090800cfe);
      if (lVar6 == 0) {
        pcVar8 = s___s__tx_cma_pa_allocation_failed_ffff0000907ff2c1;
LAB_ffff0000907486ac:
        FUN_ffff00009077de54(pcVar8,s_wifi_post_allocation_ffff0000907f1640);
        return 0xfffffffb;
      }
    }
    FUN_ffff00009077de54
              (s___s__tx_cma_pa___lX__ffff0000907d7cad,s_wifi_post_allocation_ffff0000907f1640,lVar6
              );
    iVar1 = FUN_ffff00009076d75c(param_1,s__soc_wifi_misc_ffff0000907ff2e3);
    pcVar8 = s__soc_wifi_misc_ffff0000907ff2e3;
    if (iVar1 < 0) {
      pcVar8 = s__wifi_misc_ffff00009080281c;
    }
    iVar1 = FUN_ffff00009071a008(pcVar8,s_shared_dma_pool_wifi_misc_reserv_ffff0000908027ef);
    if (iVar1 != 0) {
      FUN_ffff00009077de54
                (s__s_wifi_misc_pre_alloc_phandle_f_ffff0000907f1655,
                 s_wifi_post_allocation_ffff0000907f1640,iVar1);
    }
    iVar1 = FUN_ffff00009076d75c(param_1,s__soc_wifi_ffff0000907fd71c);
    pcVar8 = s__soc_wifi_ffff0000907fd71c;
    if (iVar1 < 0) {
      pcVar8 = s__wifi_ffff0000907e4ca1;
    }
    iVar1 = FUN_ffff00009071a008(pcVar8,s_shared_dma_pool_wifi_reserve_mem_ffff0000907fba5c);
    if (iVar1 != 0) {
      FUN_ffff00009077de54
                (s__s_pre_alloc_phandle_failed___re_ffff0000907eaf88,
                 s_wifi_post_allocation_ffff0000907f1640,iVar1);
    }
    if (lVar3 != 0) {
      iVar1 = FUN_ffff00009076d75c(param_1,s__soc_wifi_page_pool_ffff0000907fba48);
      pcVar8 = s__soc_wifi_page_pool_ffff0000907fba48;
      if (iVar1 < 0) {
        pcVar8 = s__wifi_page_pool_ffff0000907f6635;
      }
      iVar1 = FUN_ffff00009071a008(pcVar8,s_shared_dma_pool_wifi_page_pool_r_ffff0000907de3aa);
      if (iVar1 != 0) {
        FUN_ffff00009077de54
                  (s__s_pre_alloc_phandle_failed___re_ffff0000907eaf88,
                   s_wifi_post_allocation_ffff0000907f1640,iVar1);
      }
    }
    if (lVar6 != 0) {
      iVar1 = FUN_ffff00009076d75c(param_1,s__soc_wifi_tx_cma_ffff0000907d6400);
      pcVar8 = s__soc_wifi_tx_cma_ffff0000907d6400;
      if (iVar1 < 0) {
        pcVar8 = s__wifi_tx_cma_ffff0000907f2e56;
      }
      iVar1 = FUN_ffff00009071a008(pcVar8,s_shared_dma_pool_wifi_tx_cma_rese_ffff000090800cfe);
      if (iVar1 != 0) {
        FUN_ffff00009077de54
                  (s__s_pre_alloc_phandle_failed___re_ffff0000907eaf88,
                   s_wifi_post_allocation_ffff0000907f1640,iVar1);
      }
    }
    pcVar8 = s___s__DONE_ffff0000907d94dc;
  }
  FUN_ffff00009077de54(pcVar8,s_wifi_post_allocation_ffff0000907f1640);
  return 0;
}


