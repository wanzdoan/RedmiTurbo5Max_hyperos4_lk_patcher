
void FUN_ffff00009075f8c0(code *param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  ulong uVar5;
  long lVar6;
  long lVar7;
  undefined4 *puVar8;
  
  lVar3 = FUN_ffff00009077d514(0x1000);
  if (lVar3 != 0) {
    lVar7 = 0;
    lVar6 = 0;
    puVar8 = (undefined4 *)&DAT_ffff00000011a800;
    do {
      iVar2 = FUN_ffff00009077f6fc(lVar3 + lVar6,0x1000 - lVar6,&DAT_ffff0000907d4d8c,*puVar8);
      uVar1 = (uint)lVar7 & 0xff;
      lVar6 = lVar6 + iVar2;
      if ((uVar1 * -0x55555555 >> 2 | uVar1 * -0x40000000) < 0x15555556) {
        iVar2 = FUN_ffff00009077f6fc(lVar3 + lVar6,0x1000 - lVar6,&DAT_ffff0000907f836a);
        lVar6 = lVar6 + iVar2;
      }
      lVar7 = lVar7 + 1;
      puVar8 = puVar8 + 1;
    } while (lVar7 != 0x100);
    uVar4 = FUN_ffff00009077b334(lVar3);
    uVar5 = (*param_1)(uVar4,lVar6);
    if ((uVar5 & 1) == 0) {
      FUN_ffff00009077e11c(s__ThermalStat_thermal_log_dump_fa_ffff0000907e9cb7);
    }
    thunk_FUN_ffff0000907c314c(lVar3);
    return;
  }
  return;
}


