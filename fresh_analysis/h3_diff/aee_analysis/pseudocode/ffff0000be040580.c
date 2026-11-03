
void FUN_ffff0000be040580(undefined4 param_1,undefined8 param_2)

{
  undefined *puVar1;
  uint uVar2;
  uint *puVar3;
  uint uVar4;
  code *pcVar5;
  ulong uVar6;
  long lVar7;
  
  puVar3 = (uint *)FUN_ffff0000be0443c4();
  uVar2 = FUN_ffff0000be0440c4(param_1);
  FUN_ffff0000be0446a4(param_1,param_2);
  puVar1 = PTR_DAT_ffff0000be0f5660;
  uVar6 = (ulong)(uVar2 - 1);
  if (0 < (int)uVar2) {
    if ((*(long *)(PTR_DAT_ffff0000be0f5660 + (ulong)*puVar3 * 8) != 0) &&
       (pcVar5 = *(code **)(*(long *)(PTR_DAT_ffff0000be0f5660 + (ulong)*puVar3 * 8) + 0xa8),
       pcVar5 != (code *)0x0)) {
      if (uVar2 - 1 == 0) {
        uVar4 = 0x102;
      }
      else {
        uVar4 = puVar3[1];
      }
      (*pcVar5)((ulong)*puVar3,0x102,uVar4,1,param_2);
    }
    if (uVar2 != 1) {
      puVar3 = puVar3 + 2;
      lVar7 = (ulong)uVar2 - 1;
      do {
        uVar6 = uVar6 - 1;
        if ((*(long *)(puVar1 + (ulong)puVar3[-1] * 8) != 0) &&
           (pcVar5 = *(code **)(*(long *)(puVar1 + (ulong)puVar3[-1] * 8) + 0xa8),
           pcVar5 != (code *)0x0)) {
          if (uVar6 == 0) {
            uVar2 = 0x102;
          }
          else {
            uVar2 = *puVar3;
          }
          (*pcVar5)((ulong)puVar3[-1],puVar3[-2],uVar2,1,param_2);
        }
        puVar3 = puVar3 + 1;
        lVar7 = lVar7 + -1;
      } while (lVar7 != 0);
    }
  }
  return;
}


