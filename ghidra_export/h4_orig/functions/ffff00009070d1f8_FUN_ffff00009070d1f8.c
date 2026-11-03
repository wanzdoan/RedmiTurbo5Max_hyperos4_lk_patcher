// ffff00009070d1f8  size=1004  FUN_ffff00009070d1f8

void FUN_ffff00009070d1f8(undefined8 *param_1)

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
  iVar1 = thunk_FUN_ffff0000907b65c4(&local_7c);
  if (iVar1 != 0) {
    pcVar7 = s_security_deny_ffff0000907e2521;
    goto LAB_ffff00009070d588;
  }
  if (local_7c == 1) {
    pcVar7 = s_Flashing_is_not_allowed_in_Lock_s_ffff0000907dbfc5;
    goto LAB_ffff00009070d588;
  }
  if (param_1 != (undefined8 *)0x0) {
    uVar2 = FUN_ffff00009077f250(s_nvram_ffff0000907ef456);
    iVar1 = FUN_ffff00009077f348(param_1,s_nvram_ffff0000907ef456,uVar2);
    if (iVar1 != 0) {
      uVar2 = FUN_ffff00009077f250(s_nvcfg_ffff0000907ea6f5);
      iVar1 = FUN_ffff00009077f348(param_1,s_nvcfg_ffff0000907ea6f5,uVar2);
      if (iVar1 != 0) {
        uVar2 = FUN_ffff00009077f250(s_proinfo_ffff0000907e8d23);
        iVar1 = FUN_ffff00009077f348(param_1,s_proinfo_ffff0000907e8d23,uVar2);
        if (iVar1 != 0) {
          uVar2 = FUN_ffff00009077f250(s_nvdata_ffff0000907fea9f);
          iVar1 = FUN_ffff00009077f348(param_1,s_nvdata_ffff0000907fea9f,uVar2);
          if (iVar1 != 0) {
            uVar2 = FUN_ffff00009077f250(s_protect1_ffff0000907edb0b);
            iVar1 = FUN_ffff00009077f348(param_1,s_protect1_ffff0000907edb0b,uVar2);
            if (iVar1 != 0) {
              uVar2 = FUN_ffff00009077f250(s_protect2_ffff0000907da588);
              iVar1 = FUN_ffff00009077f348(param_1,s_protect2_ffff0000907da588,uVar2);
              if (iVar1 != 0) {
                uVar2 = FUN_ffff00009077f250(s_persist_ffff0000907e74b2);
                iVar1 = FUN_ffff00009077f348(param_1,s_persist_ffff0000907e74b2,uVar2);
                if (iVar1 != 0) {
                  uVar2 = FUN_ffff00009077f250(s_oem_misc1_ffff0000907d5809);
                  iVar1 = FUN_ffff00009077f348(param_1,s_oem_misc1_ffff0000907d5809,uVar2);
                  if (iVar1 != 0) {
                    uVar2 = FUN_ffff00009077f250(s_blackbox_ffff0000907f0d8c);
                    iVar1 = FUN_ffff00009077f348(param_1,s_blackbox_ffff0000907f0d8c,uVar2);
                    if (iVar1 != 0) {
                      uVar2 = FUN_ffff00009077f250(s_display_dbi_ffff0000907e252f);
                      iVar1 = FUN_ffff00009077f348(param_1,s_display_dbi_ffff0000907e252f,uVar2);
                      if (iVar1 != 0) goto LAB_ffff00009070d3cc;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    pcVar7 = s_Flashing_is_not_allowed_for_Prot_ffff0000907e0b21;
    goto LAB_ffff00009070d588;
  }
LAB_ffff00009070d3cc:
  lVar3 = FUN_ffff00009077f0d4(&local_78,param_1);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c(s_strcpy_partition_name_from_arg_f_ffff0000907fcbb5);
    return;
  }
  lVar3 = FUN_ffff00009077f544(&local_78,&DAT_ffff0000907f0d88);
  if (lVar3 != 0) {
    lVar4 = FUN_ffff00009077f250(&local_78);
    *(undefined1 *)((long)&local_7c + lVar4 + 3) = 0;
    param_1 = &local_78;
    lVar4 = FUN_ffff00009077f250(&local_78);
    *(undefined1 *)((long)&local_7c + lVar4 + 3) = 0x61;
  }
  uVar5 = FUN_ffff00009070e1a8(param_1);
  if ((uVar5 & 1) == 0) {
LAB_ffff00009070d5b4:
    pcVar7 = s_partition_is_not_writable_ffff0000907d3e8b;
  }
  else {
    lVar4 = FUN_ffff00009077c028(param_1);
    if ((lVar4 != 0) || (lVar4 = FUN_ffff00009077bf18(param_1), lVar4 != 0)) {
      lVar6 = FUN_ffff00009077c1cc(lVar4,0,*(undefined8 *)(lVar4 + 0x20));
      if (lVar6 < 0) {
LAB_ffff00009070d5c0:
        FUN_ffff000090709c9c(s_erase_failed_ffff0000907f7bb7);
        FUN_ffff00009077bfac(lVar4);
        return;
      }
      iVar1 = FUN_ffff00009077f0ac(param_1,s_userdata_ffff0000907ea6b3);
      if (iVar1 == 0) {
        FUN_ffff000090709a58(&DAT_ffff0000907d3e22,&DAT_ffff0000907df35d);
      }
      if ((*(char *)(lVar4 + 0x50) == '\0') &&
         (iVar1 = FUN_ffff000090781160(*(undefined8 *)(lVar4 + 0x18)), 0 < iVar1)) {
        FUN_ffff00009077cb0c();
      }
      FUN_ffff00009077e11c(s_erase_partition__s_down_ffff0000907dd968,param_1);
      FUN_ffff00009077bfac(lVar4);
      if (lVar3 == 0) {
LAB_ffff00009070d570:
        FUN_ffff000090709cdc(&DAT_ffff0000907df338);
        return;
      }
      lVar3 = FUN_ffff00009077f250(&local_78);
      *(undefined1 *)((long)&local_7c + lVar3 + 3) = 0x62;
      uVar5 = FUN_ffff00009070e1a8(&local_78);
      if ((uVar5 & 1) == 0) goto LAB_ffff00009070d5b4;
      lVar4 = FUN_ffff00009077c028(&local_78);
      if ((lVar4 != 0) || (lVar4 = FUN_ffff00009077bf18(&local_78), lVar4 != 0)) {
        lVar3 = FUN_ffff00009077c1cc(lVar4,0,*(undefined8 *)(lVar4 + 0x20));
        if (lVar3 < 0) goto LAB_ffff00009070d5c0;
        iVar1 = FUN_ffff00009077f0ac(&local_78,s_userdata_ffff0000907ea6b3);
        if (iVar1 == 0) {
          FUN_ffff000090709a58(&DAT_ffff0000907d3e22,&DAT_ffff0000907df35d);
        }
        if ((*(char *)(lVar4 + 0x50) == '\0') &&
           (iVar1 = FUN_ffff000090781160(*(undefined8 *)(lVar4 + 0x18)), 0 < iVar1)) {
          FUN_ffff00009077cb0c();
        }
        FUN_ffff00009077e11c(s_erase_partition__s_down_ffff0000907dd968,&local_78);
        FUN_ffff00009077bfac(lVar4);
        goto LAB_ffff00009070d570;
      }
    }
    pcVar7 = s_partition_does_not_exist_ffff0000907fe9ff;
  }
LAB_ffff00009070d588:
  FUN_ffff000090709c9c(pcVar7);
  return;
}


