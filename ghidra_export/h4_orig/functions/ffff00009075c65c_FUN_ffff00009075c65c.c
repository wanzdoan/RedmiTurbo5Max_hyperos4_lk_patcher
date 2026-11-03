// ffff00009075c65c  size=340  FUN_ffff00009075c65c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c65c(void)

{
  long lVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  int iVar4;
  undefined4 uVar5;
  ulong uVar6;
  uint uVar7;
  
  puVar2 = _DAT_ffff00009087bf28;
  iVar4 = FUN_ffff00009075cec0();
  puVar3 = _DAT_ffff00009087bf28;
  if (iVar4 == 0) {
    lVar1 = (long)_DAT_ffff00009087bf28 + (ulong)_DAT_ffff00009087bf30 * 3;
    uVar6 = FUN_ffff000090781c0c();
    uVar7 = _DAT_ffff00009087bf30;
    if (((uVar6 & 1) != 0) &&
       (iVar4 = FUN_ffff00009078395c(), uVar7 = _DAT_ffff00009087bf30, iVar4 == 0xfc11)) {
      uVar7 = 0;
    }
    FUN_ffff00009077eb20(puVar3,lVar1,uVar7);
  }
  else {
    FUN_ffff00009077e11c(&DAT_ffff0000907f0ca5,s_mt_disp_update2_ffff0000907cfe99);
    FUN_ffff00009075dd10(_DAT_ffff00009087bf30 * 3 + _DAT_ffff00009087bf34);
    puVar3 = _DAT_ffff00009087bf28;
    lVar1 = (long)_DAT_ffff00009087bf28 + (ulong)_DAT_ffff00009087bf30 * 3;
    uVar6 = FUN_ffff000090781c0c();
    uVar7 = _DAT_ffff00009087bf30;
    if (((uVar6 & 1) != 0) &&
       (iVar4 = FUN_ffff00009078395c(), uVar7 = _DAT_ffff00009087bf30, iVar4 == 0xfc11)) {
      uVar7 = 0;
    }
    FUN_ffff00009077eb20(puVar3,lVar1,uVar7);
    puVar3 = _DAT_ffff00009087bf28;
    uVar5 = FUN_ffff00009075e264();
    FUN_ffff000090707c50(puVar3,uVar5);
    FUN_ffff00009077e11c(s__s_set_fb0_to_layer0_ffff0000907cfea9,s_mt_disp_update2_ffff0000907cfe99)
    ;
    FUN_ffff00009075dd10(_DAT_ffff00009087bf34);
  }
  FUN_ffff00009077e11c
            (s_fb_dump__0x_08x__0x_08x__0x_08x__ffff0000907ece4b,*puVar2,puVar2[1],puVar2[2],
             puVar2[3]);
  puVar2 = _DAT_ffff00009087bf28;
  uVar5 = FUN_ffff00009075e264();
  FUN_ffff000090707c50(puVar2,uVar5);
  FUN_ffff00009075da38(1);
  return;
}


