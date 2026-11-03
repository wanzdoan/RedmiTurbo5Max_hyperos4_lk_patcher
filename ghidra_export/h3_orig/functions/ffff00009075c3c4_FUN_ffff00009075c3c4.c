// ffff00009075c3c4  size=340  FUN_ffff00009075c3c4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c3c4(void)

{
  long lVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  int iVar4;
  undefined4 uVar5;
  ulong uVar6;
  uint uVar7;
  
  puVar2 = _DAT_ffff00009087be90;
  iVar4 = FUN_ffff00009075cc28();
  puVar3 = _DAT_ffff00009087be90;
  if (iVar4 == 0) {
    lVar1 = (long)_DAT_ffff00009087be90 + (ulong)_DAT_ffff00009087be98 * 3;
    uVar6 = FUN_ffff000090781944();
    uVar7 = _DAT_ffff00009087be98;
    if (((uVar6 & 1) != 0) &&
       (iVar4 = FUN_ffff000090783694(), uVar7 = _DAT_ffff00009087be98, iVar4 == 0xfc11)) {
      uVar7 = 0;
    }
    FUN_ffff00009077e858(puVar3,lVar1,uVar7);
  }
  else {
    FUN_ffff00009077de54(&DAT_ffff0000907f0b34,s_mt_disp_update2_ffff0000907cfe7a);
    FUN_ffff00009075da78(_DAT_ffff00009087be98 * 3 + _DAT_ffff00009087be9c);
    puVar3 = _DAT_ffff00009087be90;
    lVar1 = (long)_DAT_ffff00009087be90 + (ulong)_DAT_ffff00009087be98 * 3;
    uVar6 = FUN_ffff000090781944();
    uVar7 = _DAT_ffff00009087be98;
    if (((uVar6 & 1) != 0) &&
       (iVar4 = FUN_ffff000090783694(), uVar7 = _DAT_ffff00009087be98, iVar4 == 0xfc11)) {
      uVar7 = 0;
    }
    FUN_ffff00009077e858(puVar3,lVar1,uVar7);
    puVar3 = _DAT_ffff00009087be90;
    uVar5 = FUN_ffff00009075dfcc();
    FUN_ffff000090707cc4(puVar3,uVar5);
    FUN_ffff00009077de54(s__s_set_fb0_to_layer0_ffff0000907cfe8a,s_mt_disp_update2_ffff0000907cfe7a)
    ;
    FUN_ffff00009075da78(_DAT_ffff00009087be9c);
  }
  FUN_ffff00009077de54
            (s_fb_dump__0x_08x__0x_08x__0x_08x__ffff0000907eccc8,*puVar2,puVar2[1],puVar2[2],
             puVar2[3]);
  puVar2 = _DAT_ffff00009087be90;
  uVar5 = FUN_ffff00009075dfcc();
  FUN_ffff000090707cc4(puVar2,uVar5);
  FUN_ffff00009075d7a0(1);
  return;
}


