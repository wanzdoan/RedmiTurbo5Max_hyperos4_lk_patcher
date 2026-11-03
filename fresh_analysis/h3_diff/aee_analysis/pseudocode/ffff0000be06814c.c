
/* WARNING: Type propagation algorithm not settling */

int FUN_ffff0000be06814c(int param_1)

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
  
  FUN_ffff0000be05fd98(s__s_IN_ffff0000be0c36ba,s_mi_set_lock_state_ffff0000be0c24ab);
  if (param_1 - 5U < 0xfffffffe) {
    FUN_ffff0000be05fd98
              (s__s_want_to_set_wrong_lock_state___ffff0000be0b04e0,
               s_mi_set_lock_state_ffff0000be0c24ab,param_1);
    return 4;
  }
  plVar2 = (long *)FUN_ffff0000be067de4();
  lVar6 = *plVar2;
  uVar7 = *(uint *)(plVar2 + 1);
  iVar1 = FUN_ffff0000be060778(lVar6,s_do_nothing_ffff0000be0bda2b,uVar7);
  if (iVar1 == 0) {
LAB_ffff0000be06827c:
    local_a38 = 0;
    local_a40 = 0;
    local_28 = 0;
    uStack_24 = 8;
    local_240[0] = uVar7;
    FUN_ffff0000be05fd98(s__s_IN_ffff0000be0acfc9,s_mi_set_sig_ffff0000be0b92b4);
    iVar1 = 1;
    if ((uVar7 < 0x101) && (lVar6 != 0)) {
      iVar1 = FUN_ffff0000be0683d4(&local_a40,&uStack_24);
      if (iVar1 == 6) {
        iVar1 = 6;
      }
      else {
        iVar1 = FUN_ffff0000be028018();
        if (iVar1 == 2) {
          lVar3 = FUN_ffff0000be074290();
          if (lVar3 == 0x400000) {
            iVar8 = 0x3fe0;
            DAT_ffff0000be132400 = 1;
          }
          else {
            if (lVar3 != 0x1000000) {
              FUN_ffff0000be05fd98(s_wrong_rpmb_size___0x_x_ffff0000be0b5c1a,lVar3);
              return 7;
            }
            lVar3 = FUN_ffff0000be0742b4(0);
            iVar8 = (int)((ulong)(lVar3 + 0xffffffe000) >> 8);
          }
          if (uVar7 == 0) {
            uVar5 = 0;
LAB_ffff0000be06836c:
            puVar4 = &local_28;
            if ((uint)uVar5 != uVar7) {
              puVar4 = local_240;
            }
          }
          else {
            uVar5 = 0;
            do {
              if (*(char *)(lVar6 + uVar5) != '\0') goto LAB_ffff0000be06836c;
              uVar5 = uVar5 + 1;
            } while (uVar7 != uVar5);
            puVar4 = &local_28;
          }
          iVar1 = FUN_ffff0000be0686e4(iVar8 + 1,puVar4,4);
          if (iVar1 == 0) {
            iVar1 = FUN_ffff0000be0686e4(iVar8 + 2,lVar6,local_240[0]);
            FUN_ffff0000be05fd98(s__s_OUT_ffff0000be0c1274,s_mi_set_sig_ffff0000be0b92b4);
          }
        }
        else {
          iVar1 = 5;
        }
      }
    }
  }
  else {
    FUN_ffff0000be060ae0(local_240,0,0x200);
    FUN_ffff0000be060ae0(&local_a40,0,0x800);
    iVar1 = FUN_ffff0000be067e54(lVar6,uVar7,local_240);
    if (iVar1 < 1) {
      FUN_ffff0000be05fd98(s_token_sig_decrypt_failed__d_ffff0000be0b7fd3,iVar1);
      if (iVar1 == 0) goto LAB_ffff0000be068274;
    }
    else {
      iVar1 = FUN_ffff0000be067fb4(local_240,&local_a40);
      *(undefined1 *)((long)&local_a40 + (long)iVar1) = 0;
      iVar1 = FUN_ffff0000be060778(&DAT_ffff0000be132408,&local_a40);
      if (iVar1 == 0) {
LAB_ffff0000be068274:
        uVar7 = *(uint *)(plVar2 + 1);
        lVar6 = *plVar2;
        goto LAB_ffff0000be06827c;
      }
      FUN_ffff0000be05fd98(s_token_mismatch_ffff0000be0aad36);
    }
    FUN_ffff0000be05fd98(s_token_verify_fail__Can_not_be_wr_ffff0000be0c24bd);
    iVar1 = 4;
  }
  return iVar1;
}


