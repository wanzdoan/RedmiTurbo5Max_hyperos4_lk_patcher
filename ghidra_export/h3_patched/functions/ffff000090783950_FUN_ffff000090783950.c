// ffff000090783950  size=952  FUN_ffff000090783950

void FUN_ffff000090783950(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  undefined4 local_44;
  undefined8 local_40;
  int local_34;
  
  iVar1 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea59a);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__s__d__failed_to_get_fdt_offset_ffff0000907ffc41,
               s_kernel_debug_cmd_init_ffff0000907fa740,0xa6);
    return;
  }
  local_44 = 0;
  lVar3 = FUN_ffff00009076dcd4(param_1,iVar1,s_kmemleak_status_ffff0000907f04b3,&local_44);
  if ((lVar3 != 0) && (iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907df58f), iVar2 == 0)) {
    FUN_ffff000090718f34(s_kmemleak_on_ffff0000907fe0d9);
  }
  local_40 = (ulong)local_40._4_4_ << 0x20;
  lVar3 = FUN_ffff00009076dcd4(param_1,iVar1,s_slub_debug_status_ffff0000907f1d03,&local_40);
  if ((lVar3 != 0) && (iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907df58f), iVar2 == 0)) {
    FUN_ffff000090718f34(s_slub_debug_OFPU_ffff0000907e396f);
  }
  local_40 = local_40 & 0xffffffff00000000;
  lVar3 = FUN_ffff00009076dcd4(param_1,iVar1,s_page_owner_status_ffff0000907f35ae,&local_40);
  if (lVar3 != 0) {
    iVar2 = FUN_ffff00009077ede4(lVar3,s_disabled_ffff0000907f0df6);
    if (iVar2 == 0) {
      pcVar4 = s_page_owner_off_ffff0000907ed0d4;
    }
    else {
      iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907df58f);
      if (iVar2 != 0) goto LAB_ffff000090783a90;
      pcVar4 = s_page_owner_on_ffff0000907f1d15;
      DAT_ffff0000908871a4 = '\x01';
    }
    FUN_ffff000090718f34(pcVar4);
  }
LAB_ffff000090783a90:
  local_34 = 0;
  local_40 = 0;
  lVar3 = FUN_ffff00009076dcd4(param_1,iVar1,s_kaslr_status_ffff0000907eb6d4,&local_34);
  if ((lVar3 == 0) || (iVar2 = FUN_ffff00009077ede4(lVar3,s_disabled_ffff0000907f0df6), iVar2 != 0))
  {
    lVar3 = FUN_ffff00009076dcd4(param_1,iVar1,s_kaslr_seed_ffff0000907f04c3,&local_34);
    if (lVar3 != 0) {
      if (local_34 != 8) {
        FUN_ffff00009077de54
                  (s__s__d__incorrect_kaslr_seed_leng_ffff0000907fa756,
                   s_kernel_kaslr_seed_init_ffff0000907f35c0,0x88);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_aee_deb_ffff00009080156e,0x89,&DAT_ffff0000907d24c9);
      }
      iVar2 = FUN_ffff000090737364(&local_40);
      if (iVar2 != 0) {
        FUN_ffff00009077de54
                  (s__s__d___TRNG__failed_at__s__ret___ffff0000907e397f,
                   s_kernel_kaslr_seed_init_ffff0000907f35c0,0x90,
                   s_kernel_kaslr_seed_init_ffff0000907f35c0,iVar2);
      }
      iVar2 = FUN_ffff000090737364((ulong)&local_40 | 4);
      if (iVar2 != 0) {
        FUN_ffff00009077de54
                  (s__s__d___TRNG__failed_at__s__ret___ffff0000907e397f,
                   s_kernel_kaslr_seed_init_ffff0000907f35c0,0x93,
                   s_kernel_kaslr_seed_init_ffff0000907f35c0,iVar2);
      }
      iVar2 = FUN_ffff00009076e40c(param_1,iVar1,s_kaslr_seed_ffff0000907f04c3,&local_40,local_34);
      if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_aee_deb_ffff00009080156e,0x99,&DAT_ffff0000907d24c9);
      }
    }
  }
  else {
    FUN_ffff000090718f34(s_nokaslr_ffff0000907fe0e5);
  }
  local_40 = local_40 & 0xffffffff00000000;
  lVar3 = FUN_ffff00009076dcd4(param_1,iVar1,s_hwtag_kasan_ffff0000907d37af,&local_40);
  if (lVar3 != 0) {
    iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907e02aa);
    if (iVar2 == 0) {
      pcVar4 = s_kasan_mode_sync_ffff0000907d1e7a;
      DAT_ffff0000908871a4 = '\x01';
    }
    else {
      iVar2 = FUN_ffff00009077ede4(lVar3,s_async_ffff0000907e83b7);
      if (iVar2 == 0) {
        pcVar4 = s_kasan_mode_async_ffff0000907f1d23;
      }
      else {
        iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907e02af);
        if (iVar2 != 0) goto LAB_ffff000090783c14;
        pcVar4 = s_kasan_off_ffff0000907dc73f;
      }
    }
    FUN_ffff000090718f34(pcVar4);
  }
LAB_ffff000090783c14:
  lVar3 = FUN_ffff00009076dcd4(param_1,iVar1,s_hwtag_kasan_stacktrace_ffff0000907f04ce,&local_40);
  if ((lVar3 != 0) && (iVar2 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907ffc62), iVar2 == 0)) {
    FUN_ffff000090718f34(s_kasan_stacktrace_on_ffff0000907d5122);
  }
  lVar3 = FUN_ffff00009076dcd4(param_1,iVar1,s_hwtag_kasan_fault_ffff0000907ed0e3,&local_40);
  if (lVar3 != 0) {
    iVar1 = FUN_ffff00009077ede4(lVar3,s_report_ffff0000907f53cb);
    if (iVar1 == 0) {
      pcVar4 = s_kasan_fault_report_ffff0000907d1e8a;
    }
    else {
      iVar1 = FUN_ffff00009077ede4(lVar3,s_panic_ffff0000907e9ee1);
      if (iVar1 != 0) goto LAB_ffff000090783ca8;
      pcVar4 = s_kasan_fault_panic_ffff00009080159c;
    }
    FUN_ffff000090718f34(pcVar4);
  }
LAB_ffff000090783ca8:
  if (DAT_ffff0000908871a4 == '\x01') {
    FUN_ffff000090718f34(s_stack_depot_disable_off_ffff0000907e6b56);
  }
  return;
}


