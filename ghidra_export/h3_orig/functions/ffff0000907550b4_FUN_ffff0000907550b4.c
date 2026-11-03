// ffff0000907550b4  size=252  FUN_ffff0000907550b4

void FUN_ffff0000907550b4(undefined4 param_1,undefined8 param_2)

{
  long lVar1;
  uint uVar2;
  uint *puVar3;
  uint uVar4;
  long lVar5;
  code *pcVar6;
  long lVar7;
  ulong uVar8;
  
  puVar3 = (uint *)FUN_ffff000090759150();
  uVar2 = FUN_ffff000090758e50(param_1);
  FUN_ffff000090759430(param_1,param_2);
  lVar1 = DAT_ffff0000908399a8;
  uVar8 = (ulong)(uVar2 - 1);
  if (0 < (int)uVar2) {
    lVar5 = *(long *)(DAT_ffff0000908399a8 + (ulong)*puVar3 * 8);
    if ((lVar5 != 0) && (pcVar6 = *(code **)(lVar5 + 0xa8), pcVar6 != (code *)0x0)) {
      if (uVar2 - 1 == 0) {
        uVar4 = 0x102;
      }
      else {
        uVar4 = puVar3[1];
      }
      (*pcVar6)((ulong)*puVar3,0x102,uVar4,1,param_2);
    }
    if (uVar2 != 1) {
      puVar3 = puVar3 + 2;
      lVar5 = (ulong)uVar2 - 1;
      do {
        uVar8 = uVar8 - 1;
        lVar7 = *(long *)(lVar1 + (ulong)puVar3[-1] * 8);
        if ((lVar7 != 0) && (pcVar6 = *(code **)(lVar7 + 0xa8), pcVar6 != (code *)0x0)) {
          if (uVar8 == 0) {
            uVar2 = 0x102;
          }
          else {
            uVar2 = *puVar3;
          }
          (*pcVar6)((ulong)puVar3[-1],puVar3[-2],uVar2,1,param_2);
        }
        puVar3 = puVar3 + 1;
        lVar5 = lVar5 + -1;
      } while (lVar5 != 0);
    }
  }
  return;
}


