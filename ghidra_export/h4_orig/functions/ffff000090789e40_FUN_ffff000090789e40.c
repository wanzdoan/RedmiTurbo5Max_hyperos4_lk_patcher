// ffff000090789e40  size=648  FUN_ffff000090789e40

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090789e40(int param_1)

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
  
  FUN_ffff00009077e11c(s__s_IN_ffff0000907fe339,s_mi_set_lock_state_ffff0000907fc69c);
  if (param_1 - 5U < 0xfffffffe) {
    FUN_ffff00009077e11c
              (s__s_want_to_set_wrong_lock_state___ffff0000907e205d,
               s_mi_set_lock_state_ffff0000907fc69c,param_1);
    return 4;
  }
  plVar2 = (long *)FUN_ffff00009078900c();
  lVar6 = *plVar2;
  uVar7 = *(uint *)(plVar2 + 1);
  iVar1 = FUN_ffff00009077eae8(lVar6,s_do_nothing_ffff0000907f3fb1,uVar7);
  if (iVar1 == 0) {
LAB_ffff000090789f70:
    local_a38 = 0;
    local_a40 = 0;
    local_28 = 0;
    uStack_24 = 8;
    local_240[0] = uVar7;
    FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_mi_set_sig_ffff0000907eeefd);
    iVar1 = 1;
    if ((uVar7 < 0x101) && (lVar6 != 0)) {
      iVar1 = FUN_ffff00009078a0c8(&local_a40,&uStack_24);
      if (iVar1 == 6) {
        iVar1 = 6;
      }
      else {
        iVar1 = FUN_ffff00009073527c();
        if (iVar1 == 2) {
          lVar3 = FUN_ffff000090796b1c();
          if (lVar3 == 0x400000) {
            iVar8 = 0x3fe0;
            _DAT_ffff000090887598 = 1;
          }
          else {
            if (lVar3 != 0x1000000) {
              FUN_ffff00009077e11c(s_wrong_rpmb_size___0x_x_ffff0000907ea175,lVar3);
              return 7;
            }
            lVar3 = FUN_ffff000090796b40(0);
            iVar8 = (int)((ulong)(lVar3 + 0xffffffe000) >> 8);
          }
          if (uVar7 == 0) {
            uVar5 = 0;
LAB_ffff00009078a060:
            puVar4 = &local_28;
            if ((uint)uVar5 != uVar7) {
              puVar4 = local_240;
            }
          }
          else {
            uVar5 = 0;
            do {
              if (*(char *)(lVar6 + uVar5) != '\0') goto LAB_ffff00009078a060;
              uVar5 = uVar5 + 1;
            } while (uVar7 != uVar5);
            puVar4 = &local_28;
          }
          iVar1 = FUN_ffff00009078a3d8(iVar8 + 1,puVar4,4);
          if (iVar1 == 0) {
            iVar1 = FUN_ffff00009078a3d8(iVar8 + 2,lVar6,local_240[0]);
            FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_mi_set_sig_ffff0000907eeefd);
          }
        }
        else {
          iVar1 = 5;
        }
      }
    }
  }
  else {
    FUN_ffff00009077ee50(local_240,0,0x200);
    FUN_ffff00009077ee50(&local_a40,0,0x800);
    iVar1 = FUN_ffff000090789504(lVar6,uVar7,local_240);
    if (iVar1 < 1) {
      FUN_ffff00009077e11c(s_token_sig_decrypt_failed__d_ffff0000907ed393,iVar1);
      if (iVar1 == 0) goto LAB_ffff000090789f68;
    }
    else {
      iVar1 = FUN_ffff000090789734(local_240,&local_a40);
      *(undefined1 *)((long)&local_a40 + (long)iVar1) = 0;
      iVar1 = FUN_ffff00009077eae8(&DAT_ffff0000908875a0,&local_a40);
      if (iVar1 == 0) {
LAB_ffff000090789f68:
        uVar7 = *(uint *)(plVar2 + 1);
        lVar6 = *plVar2;
        goto LAB_ffff000090789f70;
      }
      FUN_ffff00009077e11c(s_token_mismatch_ffff0000907d9ebb);
    }
    FUN_ffff00009077e11c(s_token_verify_fail__Can_not_be_wr_ffff0000907fc6ae);
    iVar1 = 4;
  }
  return iVar1;
}


