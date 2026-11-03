// ffff000090789b80  size=648  FUN_ffff000090789b80

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090789b80(int param_1)

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
  
  FUN_ffff00009077de54(s__s_IN_ffff0000907fe191,s_mi_set_lock_state_ffff0000907fc500);
  if (param_1 - 5U < 0xfffffffe) {
    FUN_ffff00009077de54
              (s__s_want_to_set_wrong_lock_state___ffff0000907e1f75,
               s_mi_set_lock_state_ffff0000907fc500,param_1);
    return 4;
  }
  plVar2 = (long *)FUN_ffff000090788d4c();
  lVar6 = *plVar2;
  uVar7 = *(uint *)(plVar2 + 1);
  iVar1 = FUN_ffff00009077e820(lVar6,s_do_nothing_ffff0000907f3e0e,uVar7);
  if (iVar1 == 0) {
LAB_ffff000090789cb0:
    local_a38 = 0;
    local_a40 = 0;
    local_28 = 0;
    uStack_24 = 8;
    local_240[0] = uVar7;
    FUN_ffff00009077de54(s__s_IN_ffff0000907dd321,s_mi_set_sig_ffff0000907eed75);
    iVar1 = 1;
    if ((uVar7 < 0x101) && (lVar6 != 0)) {
      iVar1 = FUN_ffff000090789e08(&local_a40,&uStack_24);
      if (iVar1 == 6) {
        iVar1 = 6;
      }
      else {
        iVar1 = FUN_ffff000090734fe4();
        if (iVar1 == 2) {
          lVar3 = FUN_ffff00009079685c();
          if (lVar3 == 0x400000) {
            iVar8 = 0x3fe0;
            _DAT_ffff000090887500 = 1;
          }
          else {
            if (lVar3 != 0x1000000) {
              FUN_ffff00009077de54(s_wrong_rpmb_size___0x_x_ffff0000907ea01a,lVar3);
              return 7;
            }
            lVar3 = FUN_ffff000090796880(0);
            iVar8 = (int)((ulong)(lVar3 + 0xffffffe000) >> 8);
          }
          if (uVar7 == 0) {
            uVar5 = 0;
LAB_ffff000090789da0:
            puVar4 = &local_28;
            if ((uint)uVar5 != uVar7) {
              puVar4 = local_240;
            }
          }
          else {
            uVar5 = 0;
            do {
              if (*(char *)(lVar6 + uVar5) != '\0') goto LAB_ffff000090789da0;
              uVar5 = uVar5 + 1;
            } while (uVar7 != uVar5);
            puVar4 = &local_28;
          }
          iVar1 = FUN_ffff00009078a118(iVar8 + 1,puVar4,4);
          if (iVar1 == 0) {
            iVar1 = FUN_ffff00009078a118(iVar8 + 2,lVar6,local_240[0]);
            FUN_ffff00009077de54(s__s_OUT_ffff0000907fa7b2,s_mi_set_sig_ffff0000907eed75);
          }
        }
        else {
          iVar1 = 5;
        }
      }
    }
  }
  else {
    FUN_ffff00009077eb88(local_240,0,0x200);
    FUN_ffff00009077eb88(&local_a40,0,0x800);
    iVar1 = FUN_ffff000090789244(lVar6,uVar7,local_240);
    if (iVar1 < 1) {
      FUN_ffff00009077de54(s_token_sig_decrypt_failed__d_ffff0000907ed1f9,iVar1);
      if (iVar1 == 0) goto LAB_ffff000090789ca8;
    }
    else {
      iVar1 = FUN_ffff000090789474(local_240,&local_a40);
      *(undefined1 *)((long)&local_a40 + (long)iVar1) = 0;
      iVar1 = FUN_ffff00009077e820(&DAT_ffff000090887508,&local_a40);
      if (iVar1 == 0) {
LAB_ffff000090789ca8:
        uVar7 = *(uint *)(plVar2 + 1);
        lVar6 = *plVar2;
        goto LAB_ffff000090789cb0;
      }
      FUN_ffff00009077de54(s_token_mismatch_ffff0000907d9e0d);
    }
    FUN_ffff00009077de54(s_token_verify_fail__Can_not_be_wr_ffff0000907fc512);
    iVar1 = 4;
  }
  return iVar1;
}


