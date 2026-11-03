// ffff0000907aa48c  size=492  FUN_ffff0000907aa48c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907aa48c(void)

{
  uint *puVar1;
  uint *puVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  long lVar7;
  ulong uVar8;
  long lVar9;
  ulong uVar10;
  uint *puVar11;
  ulong uVar12;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 local_58;
  undefined8 local_50;
  undefined8 local_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  local_48 = 0;
  local_60 = 0;
  local_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  FUN_ffff00009077e11c(s__Sec__We_are_going_to_enter_ATF_f_ffff0000907ea38d);
  uVar6 = _DAT_ffff00009090cacc;
  if (_DAT_ffff00009090cacc < 4) goto LAB_ffff0000907aa5a0;
  uVar4 = _DAT_ffff00009090cacc >> 2;
  uVar3 = uVar4;
  if (uVar4 < 2) {
    uVar3 = 1;
  }
  uVar8 = (ulong)uVar3;
  if (uVar3 < 2) {
    uVar10 = 0;
LAB_ffff0000907aa534:
    lVar7 = uVar8 - uVar10;
    lVar9 = uVar10 * 4 + -0xffff6f6f3561;
    puVar11 = (uint *)((long)&local_60 + uVar10 * 4);
    do {
      puVar2 = (uint *)(lVar9 + -3);
      lVar7 = lVar7 + -1;
      lVar9 = lVar9 + 4;
      uVar3 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
      *puVar11 = uVar3 >> 0x10 | uVar3 << 0x10;
      puVar11 = puVar11 + 1;
    } while (lVar7 != 0);
  }
  else {
    uVar10 = uVar8 & 0x3ffffffe;
    lVar9 = -0xffff6f6f355d;
    puVar11 = (uint *)((ulong)&local_60 | 4);
    uVar12 = uVar10;
    do {
      puVar2 = (uint *)(lVar9 + -7);
      uVar12 = uVar12 - 2;
      puVar1 = (uint *)(lVar9 + -3);
      lVar9 = lVar9 + 8;
      uVar3 = (*puVar2 & 0xff00ff00) >> 8 | (*puVar2 & 0xff00ff) << 8;
      uVar5 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
      puVar11[-1] = uVar3 >> 0x10 | uVar3 << 0x10;
      *puVar11 = uVar5 >> 0x10 | uVar5 << 0x10;
      puVar11 = puVar11 + 2;
    } while (uVar12 != 0);
    if (uVar10 != uVar8) goto LAB_ffff0000907aa534;
  }
  if (3 < uVar6) {
    uVar8 = 0;
    if (uVar4 < 2) {
      uVar4 = 1;
    }
    do {
      FUN_ffff00009077e11c
                (s_Check_lte_cert_pubk_hash__d___0x_ffff0000907e0696,uVar8 & 0xffffffff,
                 *(undefined4 *)((long)&local_60 + uVar8 * 4));
      uVar8 = uVar8 + 1;
    } while (uVar4 != uVar8);
  }
LAB_ffff0000907aa5a0:
  FUN_ffff00009071ebf4
            (0xc2000102,0,local_60 & 0xffffffff,local_60._4_4_,local_58 & 0xffffffff,0,0,0,&local_80
            );
  FUN_ffff00009071ebf4
            (0xc2000102,1,local_58._4_4_,local_50 & 0xffffffff,local_50._4_4_,0,0,0,&local_80);
  FUN_ffff00009071ebf4
            (0xc2000102,2,local_48 & 0xffffffff,local_48._4_4_,local_40 & 0xffffffff,0,0,0,&local_80
            );
  FUN_ffff00009071ebf4
            (0xc2000102,3,local_40._4_4_,uStack_38 & 0xffffffff,uStack_38._4_4_,0,0,0,&local_80);
  FUN_ffff00009077e11c(s_set_LTE_pubk_hash_done_ffff0000907fc8b8);
  FUN_ffff00009077e11c(s__Sec__We_are_back_from_ATF_ffff0000907facaa);
  return 0;
}


