// ffff0000907420f8  size=376  FUN_ffff0000907420f8

void FUN_ffff0000907420f8(void)

{
  long lVar1;
  int iVar2;
  long *plVar3;
  char *pcVar4;
  long local_28;
  
  plVar3 = (long *)FUN_ffff00009071a204(s_system_bl31_dtb_ffff0000907dc969,0);
  if (plVar3 == (long *)0x0) {
    FUN_ffff00009077de54
              (s___s___s_mblock__s_query_failed__ffff0000907ec6d7,s_TFA_LK_ffff0000907cf9cc,
               s_platform_release_bl31_dtb_ffff0000907f1565,s_system_bl31_dtb_ffff0000907dc969);
    return;
  }
  if (*plVar3 == 0) {
    FUN_ffff00009077de54
              (s___s__mblock__s_query_pa_0__ffff000090802778,s_TFA_LK_ffff0000907cf9cc,
               s_system_bl31_dtb_ffff0000907dc969);
    return;
  }
  local_28 = 0;
  iVar2 = FUN_ffff00009077b79c
                    (DAT_ffff000090839908,s_vm_bl31_dtb_reserved_ffff0000907f4a55,0x100000,&local_28
                     ,0xc,*plVar3,0,0);
  lVar1 = local_28;
  if (iVar2 == 0) {
    if (local_28 == 0) goto LAB_ffff000090742230;
    FUN_ffff00009077de54
              (s___s__clear__s_at__p__ffff0000907f2d5e,s_TFA_LK_ffff0000907cf9cc,
               s_system_bl31_dtb_ffff0000907dc969,local_28);
    FUN_ffff00009077eb88(lVar1,0,0x100000);
    iVar2 = FUN_ffff00009077bb68(DAT_ffff000090839908,lVar1);
    if (iVar2 == 0) goto LAB_ffff000090742230;
    pcVar4 = s__s_vmm_free_region_failed__err____ffff0000907e2faa;
  }
  else {
    pcVar4 = s__s_vmm_alloc_physical_failed__er_ffff0000907e1702;
  }
  FUN_ffff00009077de54(pcVar4,s_TFA_LK_ffff0000907cf9cc,iVar2);
LAB_ffff000090742230:
  FUN_ffff00009077de54
            (s___s___s_mblock__s_free__ffff0000907de2df,s_TFA_LK_ffff0000907cf9cc,
             s_platform_release_bl31_dtb_ffff0000907f1565,s_system_bl31_dtb_ffff0000907dc969);
  FUN_ffff00009071a6ac(*plVar3);
  return;
}


