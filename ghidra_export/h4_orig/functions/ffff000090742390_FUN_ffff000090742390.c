// ffff000090742390  size=376  FUN_ffff000090742390

void FUN_ffff000090742390(void)

{
  long lVar1;
  int iVar2;
  long *plVar3;
  char *pcVar4;
  long local_28;
  
  plVar3 = (long *)FUN_ffff00009071a348(s_system_bl31_dtb_ffff0000907dca3f,0);
  if (plVar3 == (long *)0x0) {
    FUN_ffff00009077e11c
              (s___s___s_mblock__s_query_failed__ffff0000907ec85a,s_TFA_LK_ffff0000907cf9eb,
               s_platform_release_bl31_dtb_ffff0000907f16d6,s_system_bl31_dtb_ffff0000907dca3f);
    return;
  }
  if (*plVar3 == 0) {
    FUN_ffff00009077e11c
              (s___s__mblock__s_query_pa_0__ffff00009080297c,s_TFA_LK_ffff0000907cf9eb,
               s_system_bl31_dtb_ffff0000907dca3f);
    return;
  }
  local_28 = 0;
  iVar2 = FUN_ffff00009077ba64
                    (DAT_ffff000090839928,s_vm_bl31_dtb_reserved_ffff0000907f4bc2,0x100000,&local_28
                     ,0xc,*plVar3,0,0);
  lVar1 = local_28;
  if (iVar2 == 0) {
    if (local_28 == 0) goto LAB_ffff0000907424c8;
    FUN_ffff00009077e11c
              (s___s__clear__s_at__p__ffff0000907f2eea,s_TFA_LK_ffff0000907cf9eb,
               s_system_bl31_dtb_ffff0000907dca3f,local_28);
    FUN_ffff00009077ee50(lVar1,0,0x100000);
    iVar2 = FUN_ffff00009077be30(DAT_ffff000090839928,lVar1);
    if (iVar2 == 0) goto LAB_ffff0000907424c8;
    pcVar4 = s__s_vmm_free_region_failed__err____ffff0000907e307b;
  }
  else {
    pcVar4 = s__s_vmm_alloc_physical_failed__er_ffff0000907e17d3;
  }
  FUN_ffff00009077e11c(pcVar4,s_TFA_LK_ffff0000907cf9eb,iVar2);
LAB_ffff0000907424c8:
  FUN_ffff00009077e11c
            (s___s___s_mblock__s_free__ffff0000907de382,s_TFA_LK_ffff0000907cf9eb,
             s_platform_release_bl31_dtb_ffff0000907f16d6,s_system_bl31_dtb_ffff0000907dca3f);
  FUN_ffff00009071a7f0(*plVar3);
  return;
}


