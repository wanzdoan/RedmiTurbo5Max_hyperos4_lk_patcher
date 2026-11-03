// ffff000090789aec  size=852  FUN_ffff000090789aec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090789aec(undefined4 *param_1)

{
  int iVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  char *pcVar5;
  undefined4 uVar6;
  ulong uVar7;
  undefined8 local_148;
  undefined8 uStack_140;
  undefined8 local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined4 local_44;
  ulong local_40;
  undefined1 local_38;
  
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  uStack_e0 = 0;
  local_f8 = 0;
  uStack_f0 = 0;
  local_108 = 0;
  uStack_100 = 0;
  local_118 = 0;
  uStack_110 = 0;
  local_128 = 0;
  uStack_120 = 0;
  local_138 = 0;
  uStack_130 = 0;
  local_148 = 0;
  uStack_140 = 0;
  FUN_ffff00009077e11c(s__s_IN_ffff0000907fe339,s_mi_get_lock_state_ffff0000907e3b88);
  local_38 = 0;
  local_40 = 0;
  local_44 = 9;
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_mi_check_magic_ffff0000907d6dc6);
  iVar1 = FUN_ffff00009078a0c8(&local_40,&local_44);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff00009077eae8(&local_40,s_Jz8PNRUF_ffff0000908366b0,8);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c(s_1st_time_run_RPMB_Lock__write_ma_ffff0000907d9ecc);
      goto LAB_ffff000090789c04;
    }
    FUN_ffff00009077e11c(s__s_PASS_ffff0000907e2083,s_mi_check_magic_ffff0000907d6dc6);
LAB_ffff000090789c6c:
    local_40 = local_40 & 0xffffffff00000000;
    FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_mi_get_sig_ffff0000907eeef2);
    iVar1 = FUN_ffff00009073527c();
    if (iVar1 == 2) {
      lVar4 = FUN_ffff000090796b1c();
      if (lVar4 == 0x400000) {
        iVar1 = 0x3fe0;
        _DAT_ffff000090887598 = 1;
      }
      else {
        if (lVar4 != 0x1000000) {
          FUN_ffff00009077e11c(s_wrong_rpmb_size___0x_x_ffff0000907ea175,lVar4);
          uVar7 = 7;
          goto LAB_ffff000090789d94;
        }
        lVar4 = FUN_ffff000090796b40(0);
        iVar1 = (int)((ulong)(lVar4 + 0xffffffe000) >> 8);
      }
      iVar3 = FUN_ffff00009078a1b8(iVar1 + 1,&local_40,4);
      if ((iVar3 == 0) && ((uint)local_40 < 0x101)) {
        uVar2 = FUN_ffff00009078a1b8(iVar1 + 2,&local_148);
        uVar7 = local_40;
        if (uVar2 == 0) {
          iVar1 = (uint)local_40;
          FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_mi_get_sig_ffff0000907eeef2);
          if (iVar1 != 0) {
            FUN_ffff00009077e11c
                      (s__s_sig_len____d_ffff0000907eeed4,s_mi_get_lock_state_ffff0000907e3b88,
                       uVar7 & 0xffffffff);
            iVar1 = FUN_ffff0000907898cc(&local_148,uVar7 & 0xffffffff);
            uVar6 = 3;
            if (iVar1 != 0) {
              uVar6 = 4;
            }
            *param_1 = uVar6;
            FUN_ffff00009077e11c(s__s_OUT_ffff0000907d867e,s_mi_get_lock_state_ffff0000907e3b88);
            return 0;
          }
          FUN_ffff00009077e11c(s_no_sig_stored_ffff0000907e559c);
          goto LAB_ffff000090789da4;
        }
        uVar7 = (ulong)uVar2;
        FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_mi_get_sig_ffff0000907eeef2);
      }
      else {
        FUN_ffff00009077e11c
                  (s_operation_failed_or_wrong_siglen_ffff0000907eb8f6,local_40 & 0xffffffff);
        uVar7 = 1;
      }
    }
    else {
      uVar7 = 5;
    }
LAB_ffff000090789d94:
    pcVar5 = s_mi_get_sig_return__d_ffff0000907f1fe0;
LAB_ffff000090789da0:
    FUN_ffff00009077e11c(pcVar5,uVar7);
  }
  else {
    FUN_ffff00009077e11c
              (s__s_failed__ret____d_ffff0000907f388b,s_mi_check_magic_ffff0000907d6dc6,iVar1);
    if (iVar1 == 3) {
LAB_ffff000090789c04:
      FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_mi_set_magic_ffff0000907e3b9a);
      iVar1 = FUN_ffff00009073527c();
      if (iVar1 == 2) {
        uVar7 = FUN_ffff000090796b1c();
        if (uVar7 == 0x400000) {
          uVar7 = 0x3fe0;
          _DAT_ffff000090887598 = 1;
        }
        else {
          if (uVar7 != 0x1000000) {
            pcVar5 = s_wrong_rpmb_size___0x_x_ffff0000907ea175;
            goto LAB_ffff000090789da0;
          }
          lVar4 = FUN_ffff000090796b40(0,0x1000000);
          uVar7 = lVar4 + 0xffffffe000U >> 8;
        }
        FUN_ffff00009078a3d8(uVar7,s_Jz8PNRUF_ffff0000908366b0,8);
        FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_mi_set_magic_ffff0000907e3b9a);
      }
    }
    else if (iVar1 != 4) {
      if (iVar1 == 6) {
        uVar2 = thunk_FUN_ffff00009074b22c(0x1b);
        uVar6 = 3;
        if (((uVar2 ^ 0xffffffff) & 0xe) == 0) {
          uVar6 = 4;
        }
        goto LAB_ffff000090789da8;
      }
      goto LAB_ffff000090789c6c;
    }
  }
LAB_ffff000090789da4:
  uVar6 = 4;
LAB_ffff000090789da8:
  *param_1 = uVar6;
  return 0;
}


