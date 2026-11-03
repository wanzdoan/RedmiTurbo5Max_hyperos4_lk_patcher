// ffff000090783c18  size=952  FUN_ffff000090783c18

void FUN_ffff000090783c18(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  undefined4 local_44;
  undefined8 local_40;
  int local_34;
  
  iVar1 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__failed_to_get_fdt_offset_ffff0000907ffde9,
               s_kernel_debug_cmd_init_ffff0000907fa8dc,0xa6);
    return;
  }
  local_44 = 0;
  lVar3 = FUN_ffff00009076df9c(param_1,iVar1,s_kmemleak_status_ffff0000907f063b,&local_44);
  if ((lVar3 != 0) && (iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907df649), iVar2 == 0)) {
    FUN_ffff000090719078(s_kmemleak_on_ffff0000907fe281);
  }
  local_40 = (ulong)local_40._4_4_ << 0x20;
  lVar3 = FUN_ffff00009076df9c(param_1,iVar1,s_slub_debug_status_ffff0000907f1ea6,&local_40);
  if ((lVar3 != 0) && (iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907df649), iVar2 == 0)) {
    FUN_ffff000090719078(s_slub_debug_OFPU_ffff0000907e3a57);
  }
  local_40 = local_40 & 0xffffffff00000000;
  lVar3 = FUN_ffff00009076df9c(param_1,iVar1,s_page_owner_status_ffff0000907f3751,&local_40);
  if (lVar3 != 0) {
    iVar2 = FUN_ffff00009077f0ac(lVar3,s_disabled_ffff0000907f0f7e);
    if (iVar2 == 0) {
      pcVar4 = s_page_owner_off_ffff0000907ed26e;
    }
    else {
      iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907df649);
      if (iVar2 != 0) goto LAB_ffff000090783d58;
      pcVar4 = s_page_owner_on_ffff0000907f1eb8;
      DAT_ffff00009088723c = '\x01';
    }
    FUN_ffff000090719078(pcVar4);
  }
LAB_ffff000090783d58:
  local_34 = 0;
  local_40 = 0;
  lVar3 = FUN_ffff00009076df9c(param_1,iVar1,s_kaslr_status_ffff0000907eb86e,&local_34);
  if ((lVar3 == 0) || (iVar2 = FUN_ffff00009077f0ac(lVar3,s_disabled_ffff0000907f0f7e), iVar2 != 0))
  {
    lVar3 = FUN_ffff00009076df9c(param_1,iVar1,s_kaslr_seed_ffff0000907f064b,&local_34);
    if (lVar3 != 0) {
      if (local_34 != 8) {
        FUN_ffff00009077e11c
                  (s__s__d__incorrect_kaslr_seed_leng_ffff0000907fa8f2,
                   s_kernel_kaslr_seed_init_ffff0000907f3763,0x88);
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_aee_deb_ffff00009080174a,0x89,&DAT_ffff0000907d2506);
      }
      iVar2 = FUN_ffff0000907375fc(&local_40);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d___TRNG__failed_at__s__ret___ffff0000907e3a67,
                   s_kernel_kaslr_seed_init_ffff0000907f3763,0x90,
                   s_kernel_kaslr_seed_init_ffff0000907f3763,iVar2);
      }
      iVar2 = FUN_ffff0000907375fc((ulong)&local_40 | 4);
      if (iVar2 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d___TRNG__failed_at__s__ret___ffff0000907e3a67,
                   s_kernel_kaslr_seed_init_ffff0000907f3763,0x93,
                   s_kernel_kaslr_seed_init_ffff0000907f3763,iVar2);
      }
      iVar2 = FUN_ffff00009076e6d4(param_1,iVar1,s_kaslr_seed_ffff0000907f064b,&local_40,local_34);
      if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_aee_deb_ffff00009080174a,0x99,&DAT_ffff0000907d2506);
      }
    }
  }
  else {
    FUN_ffff000090719078(s_nokaslr_ffff0000907fe28d);
  }
  local_40 = local_40 & 0xffffffff00000000;
  lVar3 = FUN_ffff00009076df9c(param_1,iVar1,s_hwtag_kasan_ffff0000907d37ec,&local_40);
  if (lVar3 != 0) {
    iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907e0364);
    if (iVar2 == 0) {
      pcVar4 = s_kasan_mode_sync_ffff0000907d1eb7;
      DAT_ffff00009088723c = '\x01';
    }
    else {
      iVar2 = FUN_ffff00009077f0ac(lVar3,s_async_ffff0000907e8512);
      if (iVar2 == 0) {
        pcVar4 = s_kasan_mode_async_ffff0000907f1ec6;
      }
      else {
        iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907e0369);
        if (iVar2 != 0) goto LAB_ffff000090783edc;
        pcVar4 = s_kasan_off_ffff0000907dc82c;
      }
    }
    FUN_ffff000090719078(pcVar4);
  }
LAB_ffff000090783edc:
  lVar3 = FUN_ffff00009076df9c(param_1,iVar1,s_hwtag_kasan_stacktrace_ffff0000907f0656,&local_40);
  if ((lVar3 != 0) && (iVar2 = FUN_ffff00009077f0ac(lVar3,&DAT_ffff0000907ffe0a), iVar2 == 0)) {
    FUN_ffff000090719078(s_kasan_stacktrace_on_ffff0000907d515f);
  }
  lVar3 = FUN_ffff00009076df9c(param_1,iVar1,s_hwtag_kasan_fault_ffff0000907ed27d,&local_40);
  if (lVar3 != 0) {
    iVar1 = FUN_ffff00009077f0ac(lVar3,s_report_ffff0000907f554f);
    if (iVar1 == 0) {
      pcVar4 = s_kasan_fault_report_ffff0000907d1ec7;
    }
    else {
      iVar1 = FUN_ffff00009077f0ac(lVar3,s_panic_ffff0000907ea03c);
      if (iVar1 != 0) goto LAB_ffff000090783f70;
      pcVar4 = s_kasan_fault_panic_ffff000090801778;
    }
    FUN_ffff000090719078(pcVar4);
  }
LAB_ffff000090783f70:
  if (DAT_ffff00009088723c == '\x01') {
    FUN_ffff000090719078(s_stack_depot_disable_off_ffff0000907e6c81);
  }
  return;
}


