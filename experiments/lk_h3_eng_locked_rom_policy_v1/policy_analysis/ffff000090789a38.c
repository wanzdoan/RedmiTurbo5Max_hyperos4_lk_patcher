
/* WARNING: Type propagation algorithm not settling */

int FUN_ffff000090789a38(int param_1)

{
  int iVar1;
  long *plVar2;
  long lVar3;
  uint *puVar4;
  ulong uVar5;
  long lVar6;
  uint uVar7;
  int iVar8;
  undefined8 local_a40;
  undefined1 local_a38;
  uint local_240 [128];
  uint local_28;
  undefined4 uStack_24;

  FUN_ffff00009077ea84(s__s_IN_ffff0000907f7128,s_mi_set_lock_state_ffff0000907f54d0);
  if (param_1 - 5U < 0xfffffffe) {
    FUN_ffff00009077ea84
              (s__s_want_to_set_wrong_lock_state___ffff0000907da743,
               s_mi_set_lock_state_ffff0000907f54d0,param_1);
    return 4;
  }
  plVar2 = (long *)FUN_ffff000090788c04();
  lVar6 = *plVar2;
  uVar7 = *(uint *)(plVar2 + 1);
  iVar1 = FUN_ffff00009077f450(lVar6,s_do_nothing_ffff0000907ecc75,uVar7);
  if (iVar1 == 0) {
LAB_ffff000090789b68:
    local_a38 = 0;
    local_a40 = 0;
    local_28 = 0;
    uStack_24 = 8;
    local_240[0] = uVar7;
    FUN_ffff00009077ea84(s__s_IN_ffff0000907d585f,s_mi_set_sig_ffff0000907e7a2e);
    iVar1 = 1;
    if ((uVar7 < 0x101) && (lVar6 != 0)) {
      iVar1 = FUN_ffff000090789cc0(&local_a40,&uStack_24);
      if (iVar1 == 6) {
        iVar1 = 6;
      }
      else {
        iVar1 = FUN_ffff00009073585c();
        if (iVar1 == 2) {
          lVar3 = FUN_ffff00009079679c();
          if (lVar3 == 0x400000) {
            iVar8 = 0x3fe0;
            DAT_ffff0000908874f8 = 1;
          }
          else {
            if (lVar3 != 0x1000000) {
              FUN_ffff00009077ea84(s_wrong_rpmb_size___0x_x_ffff0000907e2aa3,lVar3);
              return 7;
            }
            lVar3 = FUN_ffff0000907967c0(0);
            iVar8 = (int)((ulong)(lVar3 + 0xffffffe000) >> 8);
          }
          if (uVar7 == 0) {
            uVar5 = 0;
LAB_ffff000090789c58:
            puVar4 = &local_28;
            if ((uint)uVar5 != uVar7) {
              puVar4 = local_240;
            }
          }
          else {
            uVar5 = 0;
            do {
              if (*(char *)(lVar6 + uVar5) != '\0') goto LAB_ffff000090789c58;
              uVar5 = uVar5 + 1;
            } while (uVar7 != uVar5);
            puVar4 = &local_28;
          }
          iVar1 = FUN_ffff000090789fd0(iVar8 + 1,puVar4,4);
          if (iVar1 == 0) {
            iVar1 = FUN_ffff000090789fd0(iVar8 + 2,lVar6,local_240[0]);
            FUN_ffff00009077ea84(s__s_OUT_ffff0000907f3795,s_mi_set_sig_ffff0000907e7a2e);
          }
        }
        else {
          iVar1 = 5;
        }
      }
    }
  }
  else {
    FUN_ffff00009077f7b8(local_240,0,0x200);
    FUN_ffff00009077f7b8(&local_a40,0,0x800);
    iVar1 = FUN_ffff0000907890fc(lVar6,uVar7,local_240);
    if (iVar1 < 1) {
      FUN_ffff00009077ea84(s_token_sig_decrypt_failed__d_ffff0000907e5e96,iVar1);
      if (iVar1 == 0) goto LAB_ffff000090789b60;
    }
    else {
      iVar1 = FUN_ffff00009078932c(local_240,&local_a40);
      *(undefined1 *)((long)&local_a40 + (long)iVar1) = 0;
      iVar1 = FUN_ffff00009077f450(&DAT_ffff000090887500,&local_a40);
      if (iVar1 == 0) {
LAB_ffff000090789b60:
        uVar7 = *(uint *)(plVar2 + 1);
        lVar6 = *plVar2;
        goto LAB_ffff000090789b68;
      }
      FUN_ffff00009077ea84(s_token_mismatch_ffff0000907d22bc);
    }
    FUN_ffff00009077ea84(s_token_verify_fail__Can_not_be_wr_ffff0000907f54e2);
    iVar1 = 4;
  }
  return iVar1;
}
