// ffff00009070d180  size=1004  FUN_ffff00009070d180

void FUN_ffff00009070d180(undefined8 *param_1)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  long lVar4;
  ulong uVar5;
  long lVar6;
  char *pcVar7;
  undefined4 local_7c;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_78 = 0;
  local_7c = 1;
  iVar1 = thunk_FUN_ffff0000907b5f54(&local_7c);
  if (iVar1 != 0) {
    pcVar7 = s_security_deny_ffff0000907e2450;
    goto LAB_ffff00009070d510;
  }
  if (local_7c == 1) {
    pcVar7 = s_Flashing_is_not_allowed_in_Lock_s_ffff0000907dbeba;
    goto LAB_ffff00009070d510;
  }
  if (param_1 != (undefined8 *)0x0) {
    uVar2 = FUN_ffff00009077ef88(s_nvram_ffff0000907ef2e5);
    iVar1 = FUN_ffff00009077f080(param_1,s_nvram_ffff0000907ef2e5,uVar2);
    if (iVar1 != 0) {
      uVar2 = FUN_ffff00009077ef88(s_nvcfg_ffff0000907ea59b);
      iVar1 = FUN_ffff00009077f080(param_1,s_nvcfg_ffff0000907ea59b,uVar2);
      if (iVar1 != 0) {
        uVar2 = FUN_ffff00009077ef88(s_proinfo_ffff0000907e8bdf);
        iVar1 = FUN_ffff00009077f080(param_1,s_proinfo_ffff0000907e8bdf,uVar2);
        if (iVar1 != 0) {
          uVar2 = FUN_ffff00009077ef88(s_nvdata_ffff0000907fe8f7);
          iVar1 = FUN_ffff00009077f080(param_1,s_nvdata_ffff0000907fe8f7,uVar2);
          if (iVar1 != 0) {
            uVar2 = FUN_ffff00009077ef88(s_protect1_ffff0000907ed99a);
            iVar1 = FUN_ffff00009077f080(param_1,s_protect1_ffff0000907ed99a,uVar2);
            if (iVar1 != 0) {
              uVar2 = FUN_ffff00009077ef88(s_protect2_ffff0000907da4f1);
              iVar1 = FUN_ffff00009077f080(param_1,s_protect2_ffff0000907da4f1,uVar2);
              if (iVar1 != 0) {
                uVar2 = FUN_ffff00009077ef88(s_persist_ffff0000907e736e);
                iVar1 = FUN_ffff00009077f080(param_1,s_persist_ffff0000907e736e,uVar2);
                if (iVar1 != 0) {
                  uVar2 = FUN_ffff00009077ef88(s_oem_misc1_ffff0000907d57b9);
                  iVar1 = FUN_ffff00009077f080(param_1,s_oem_misc1_ffff0000907d57b9,uVar2);
                  if (iVar1 != 0) {
                    uVar2 = FUN_ffff00009077ef88(s_blackbox_ffff0000907f0c1b);
                    iVar1 = FUN_ffff00009077f080(param_1,s_blackbox_ffff0000907f0c1b,uVar2);
                    if (iVar1 != 0) {
                      uVar2 = FUN_ffff00009077ef88(s_display_dbi_ffff0000907e245e);
                      iVar1 = FUN_ffff00009077f080(param_1,s_display_dbi_ffff0000907e245e,uVar2);
                      if (iVar1 != 0) goto LAB_ffff00009070d354;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    pcVar7 = s_Flashing_is_not_allowed_for_Prot_ffff0000907e0a7e;
    goto LAB_ffff00009070d510;
  }
LAB_ffff00009070d354:
  lVar3 = FUN_ffff00009077ee0c(&local_78,param_1);
  if (lVar3 == 0) {
    FUN_ffff00009077de54(s_strcpy_partition_name_from_arg_f_ffff0000907fca0d);
    return;
  }
  lVar3 = FUN_ffff00009077f27c(&local_78,&DAT_ffff0000907f0c17);
  if (lVar3 != 0) {
    lVar4 = FUN_ffff00009077ef88(&local_78);
    *(undefined1 *)((long)&local_7c + lVar4 + 3) = 0;
    param_1 = &local_78;
    lVar4 = FUN_ffff00009077ef88(&local_78);
    *(undefined1 *)((long)&local_7c + lVar4 + 3) = 0x61;
  }
  uVar5 = FUN_ffff00009070e130(param_1);
  if ((uVar5 & 1) == 0) {
LAB_ffff00009070d53c:
    pcVar7 = s_partition_is_not_writable_ffff0000907d3e4e;
  }
  else {
    lVar4 = FUN_ffff00009077bd60(param_1);
    if ((lVar4 != 0) || (lVar4 = FUN_ffff00009077bc50(param_1), lVar4 != 0)) {
      lVar6 = FUN_ffff00009077bf04(lVar4,0,*(undefined8 *)(lVar4 + 0x20));
      if (lVar6 < 0) {
LAB_ffff00009070d548:
        FUN_ffff000090709d10(s_erase_failed_ffff0000907f7a2d);
        FUN_ffff00009077bce4(lVar4);
        return;
      }
      iVar1 = FUN_ffff00009077ede4(param_1,s_userdata_ffff0000907ea559);
      if (iVar1 == 0) {
        FUN_ffff000090709acc(&DAT_ffff0000907d3de5,&DAT_ffff0000907df2ba);
      }
      if ((*(char *)(lVar4 + 0x50) == '\0') &&
         (iVar1 = FUN_ffff000090780e98(*(undefined8 *)(lVar4 + 0x18)), 0 < iVar1)) {
        FUN_ffff00009077c844();
      }
      FUN_ffff00009077de54(s_erase_partition__s_down_ffff0000907dd8c5,param_1);
      FUN_ffff00009077bce4(lVar4);
      if (lVar3 == 0) {
LAB_ffff00009070d4f8:
        FUN_ffff000090709d50(&DAT_ffff0000907df295);
        return;
      }
      lVar3 = FUN_ffff00009077ef88(&local_78);
      *(undefined1 *)((long)&local_7c + lVar3 + 3) = 0x62;
      uVar5 = FUN_ffff00009070e130(&local_78);
      if ((uVar5 & 1) == 0) goto LAB_ffff00009070d53c;
      lVar4 = FUN_ffff00009077bd60(&local_78);
      if ((lVar4 != 0) || (lVar4 = FUN_ffff00009077bc50(&local_78), lVar4 != 0)) {
        lVar3 = FUN_ffff00009077bf04(lVar4,0,*(undefined8 *)(lVar4 + 0x20));
        if (lVar3 < 0) goto LAB_ffff00009070d548;
        iVar1 = FUN_ffff00009077ede4(&local_78,s_userdata_ffff0000907ea559);
        if (iVar1 == 0) {
          FUN_ffff000090709acc(&DAT_ffff0000907d3de5,&DAT_ffff0000907df2ba);
        }
        if ((*(char *)(lVar4 + 0x50) == '\0') &&
           (iVar1 = FUN_ffff000090780e98(*(undefined8 *)(lVar4 + 0x18)), 0 < iVar1)) {
          FUN_ffff00009077c844();
        }
        FUN_ffff00009077de54(s_erase_partition__s_down_ffff0000907dd8c5,&local_78);
        FUN_ffff00009077bce4(lVar4);
        goto LAB_ffff00009070d4f8;
      }
    }
    pcVar7 = s_partition_does_not_exist_ffff0000907fe857;
  }
LAB_ffff00009070d510:
  FUN_ffff000090709d10(pcVar7);
  return;
}


