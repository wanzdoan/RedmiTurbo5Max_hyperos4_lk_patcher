
ulong FUN_ffff0000b804b264(long param_1,long param_2,ulong param_3)

{
  ulong uVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  
  lVar2 = FUN_ffff0000b804bd14(0x40,*(undefined8 *)(param_1 + 0x28));
  if (lVar2 == 0) {
    uVar5 = 0xfffffffffffffffb;
  }
  else {
    FUN_ffff0000b804d070(lVar2,*(undefined1 *)(param_1 + 0x68),*(undefined8 *)(param_1 + 0x28));
    uVar6 = 0;
    do {
      uVar5 = uVar6;
      if (param_3 == 0) break;
      uVar4 = 0;
      uVar1 = param_3;
      if (*(ulong *)(param_1 + 0x28) <= param_3) {
        uVar1 = *(ulong *)(param_1 + 0x28);
      }
      if (((-1 < param_2) && (uVar1 != 0)) && (lVar3 = *(long *)(param_1 + 0x20), param_2 < lVar3))
      {
        uVar5 = uVar1;
        if ((lVar3 < (long)(uVar1 + param_2)) && (uVar5 = lVar3 - param_2, uVar5 == 0)) {
          uVar4 = 0;
        }
        else {
          uVar4 = (**(code **)(param_1 + 0x80))(param_1,lVar2,param_2,uVar5);
          uVar5 = uVar4;
          if ((long)uVar4 < 0) break;
        }
      }
      uVar6 = uVar4 + uVar6;
      param_2 = uVar4 + param_2;
      param_3 = param_3 - uVar4;
      uVar5 = uVar6;
    } while (uVar1 <= uVar4);
    thunk_FUN_ffff0000b8082558(lVar2);
  }
  return uVar5;
}


