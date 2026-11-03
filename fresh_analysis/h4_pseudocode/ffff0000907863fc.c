
int FUN_ffff0000907863fc(long *param_1)

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
  uVar7 = FUN_ffff0000907206a0();
  bVar4 = (uVar7 & 1) == 0;
  pcVar2 = s_logo2_ffff0000907eee4e;
  if (bVar4) {
    pcVar2 = s_logo1_ffff000090802ff7;
  }
  uVar3 = 0x1000000;
  if (bVar4) {
    uVar3 = 0x100000;
  }
  FUN_ffff00009077f6fc(&local_18,6,pcVar2);
  lVar8 = FUN_ffff0000907198dc
                    (uVar3,0x10000,0xffffffffffffffff,0,0,
                     s_display_others_boot_logo_compres_ffff0000907ea09b);
  *param_1 = lVar8;
  if (lVar8 == 0) {
LAB_ffff000090786624:
    FUN_ffff00009077e11c(s_logo_addr_memory_failed_to_alloc_ffff0000907e3ae7);
  }
  else {
    plVar1 = param_1 + 2;
    iVar5 = FUN_ffff00009077ba64
                      (PTR_DAT_ffff000090839928,s_vm_logo_reserved_ffff0000907ea0bf,uVar3,plVar1,0xc
                       ,lVar8,0,0);
    lVar8 = param_1[2];
    if (iVar5 != 0) {
LAB_ffff000090786674:
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s_err__d__vm_logo__0x_p__ffff0000907d861d,iVar5,lVar8);
    }
    uVar6 = FUN_ffff000090785d18(&DAT_ffff0000907e3ae2,&local_18,lVar8,uVar3);
    if ((int)uVar6 < 0) {
      FUN_ffff00009077e11c(s__s_not_loaded__trying_to_load_si_ffff0000907fc5d2,&local_18);
      FUN_ffff00009071a7f0(*param_1);
      FUN_ffff00009077be30(PTR_DAT_ffff000090839928,param_1[2]);
      lVar8 = FUN_ffff0000907198dc
                        (0x1000000,0x10000,0xffffffffffffffff,0,0,
                         s_display_others_boot_logo_compres_ffff0000907ea09b);
      *param_1 = lVar8;
      if (lVar8 == 0) goto LAB_ffff000090786624;
      iVar5 = FUN_ffff00009077ba64
                        (PTR_DAT_ffff000090839928,s_vm_logo_reserved_ffff0000907ea0bf,0x1000000,
                         plVar1,0xc,lVar8,0,0);
      lVar8 = *plVar1;
      if (iVar5 != 0) goto LAB_ffff000090786674;
      iVar5 = FUN_ffff000090785d18(&DAT_ffff0000907e3ae2,&DAT_ffff0000907e3ae2,lVar8,0x1000000);
      if (iVar5 < 0) {
        FUN_ffff00009077e11c(s_logo_bin_not_loaded_ffff000090802ffd);
        FUN_ffff00009071a7f0(*param_1);
        FUN_ffff00009077be30(PTR_DAT_ffff000090839928,param_1[2]);
        return iVar5;
      }
    }
    FUN_ffff0000907c3764(~uVar6 >> 0x1f);
    iVar5 = FUN_ffff00009075c61c();
    uVar6 = iVar5 + 0xffffU & 0xffff0000;
    lVar8 = FUN_ffff0000907198dc
                      (uVar6,0x10000,0xffffffffffffffff,0,0,
                       s_display_others_boot_logo_decompr_ffff0000907d51c6);
    param_1[1] = lVar8;
    if (lVar8 != 0) {
      iVar5 = FUN_ffff00009077ba64
                        (PTR_DAT_ffff000090839928,s_vm_dec_logo_reserved_ffff0000907d1f2b,uVar6,
                         param_1 + 3,0xc,lVar8,0,0);
      if (iVar5 == 0) {
        return 0;
      }
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s_err__d__vm_dec_logo__0x_p__ffff0000907fe2d6,iVar5,param_1[3]);
    }
    FUN_ffff00009077e11c(s_dec_logo_addr_memory_failed_to_a_ffff0000907d02a3);
    FUN_ffff00009071a7f0(*param_1);
    FUN_ffff00009077be30(PTR_DAT_ffff000090839928,param_1[2]);
  }
  return -5;
}


