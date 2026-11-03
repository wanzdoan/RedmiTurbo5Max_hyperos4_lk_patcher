// ffff000090786134  size=648  FUN_ffff000090786134

int FUN_ffff000090786134(long *param_1)

{
  long *plVar1;
  char *pcVar2;
  undefined8 uVar3;
  bool bVar4;
  int iVar5;
  uint uVar6;
  ulong uVar7;
  long lVar8;
  undefined4 local_18;
  undefined2 local_14;
  
  local_14 = 0;
  local_18 = 0;
  uVar7 = FUN_ffff000090720518();
  bVar4 = (uVar7 & 1) == 0;
  pcVar2 = s_logo2_ffff0000907eecc6;
  if (bVar4) {
    pcVar2 = s_logo1_ffff000090802df3;
  }
  uVar3 = 0x1000000;
  if (bVar4) {
    uVar3 = 0x100000;
  }
  FUN_ffff00009077f434(&local_18,6,pcVar2);
  lVar8 = FUN_ffff000090719798
                    (uVar3,0x10000,0xffffffffffffffff,0,0,
                     s_display_others_boot_logo_compres_ffff0000907e9f40);
  *param_1 = lVar8;
  if (lVar8 == 0) {
LAB_ffff00009078635c:
    FUN_ffff00009077de54(s_logo_addr_memory_failed_to_alloc_ffff0000907e39ff);
  }
  else {
    plVar1 = param_1 + 2;
    iVar5 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_vm_logo_reserved_ffff0000907e9f64,uVar3,plVar1,0xc,
                       lVar8,0,0);
    lVar8 = param_1[2];
    if (iVar5 != 0) {
LAB_ffff0000907863ac:
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s_err__d__vm_logo__0x_p__ffff0000907d8587,iVar5,lVar8);
    }
    uVar6 = FUN_ffff000090785a50(&DAT_ffff0000907e39fa,&local_18,lVar8,uVar3);
    if ((int)uVar6 < 0) {
      FUN_ffff00009077de54(s__s_not_loaded__trying_to_load_si_ffff0000907fc436,&local_18);
      FUN_ffff00009071a6ac(*param_1);
      FUN_ffff00009077bb68(DAT_ffff000090839908,param_1[2]);
      lVar8 = FUN_ffff000090719798
                        (0x1000000,0x10000,0xffffffffffffffff,0,0,
                         s_display_others_boot_logo_compres_ffff0000907e9f40);
      *param_1 = lVar8;
      if (lVar8 == 0) goto LAB_ffff00009078635c;
      iVar5 = FUN_ffff00009077b79c
                        (DAT_ffff000090839908,s_vm_logo_reserved_ffff0000907e9f64,0x1000000,plVar1,
                         0xc,lVar8,0,0);
      lVar8 = *plVar1;
      if (iVar5 != 0) goto LAB_ffff0000907863ac;
      iVar5 = FUN_ffff000090785a50(&DAT_ffff0000907e39fa,&DAT_ffff0000907e39fa,lVar8,0x1000000);
      if (iVar5 < 0) {
        FUN_ffff00009077de54(s_logo_bin_not_loaded_ffff000090802df9);
        FUN_ffff00009071a6ac(*param_1);
        FUN_ffff00009077bb68(DAT_ffff000090839908,param_1[2]);
        return iVar5;
      }
    }
    FUN_ffff0000907c30f0(~uVar6 >> 0x1f);
    iVar5 = FUN_ffff00009075c384();
    uVar6 = iVar5 + 0xffffU & 0xffff0000;
    lVar8 = FUN_ffff000090719798
                      (uVar6,0x10000,0xffffffffffffffff,0,0,
                       s_display_others_boot_logo_decompr_ffff0000907d5189);
    param_1[1] = lVar8;
    if (lVar8 != 0) {
      iVar5 = FUN_ffff00009077b79c
                        (DAT_ffff000090839908,s_vm_dec_logo_reserved_ffff0000907d1eee,uVar6,
                         param_1 + 3,0xc,lVar8,0,0);
      if (iVar5 == 0) {
        return 0;
      }
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cc18(s_err__d__vm_dec_logo__0x_p__ffff0000907fe12e,iVar5,param_1[3]);
    }
    FUN_ffff00009077de54(s_dec_logo_addr_memory_failed_to_a_ffff0000907d0284);
    FUN_ffff00009071a6ac(*param_1);
    FUN_ffff00009077bb68(DAT_ffff000090839908,param_1[2]);
  }
  return -5;
}


