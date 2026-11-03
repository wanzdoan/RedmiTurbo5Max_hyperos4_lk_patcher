// ffff00009071ed28  size=84  FUN_ffff00009071ed28

undefined8 FUN_ffff00009071ed28(undefined8 param_1,uint param_2)

{
  undefined8 uVar1;
  long lVar2;
  code *pcVar3;
  
  if (((param_2 < 2) && (lVar2 = *(long *)((ulong)param_2 * 8 + -0xffff6f79fa90), lVar2 != 0)) &&
     (lVar2 = *(long *)(lVar2 + 0x18), lVar2 != 0)) {
    pcVar3 = *(code **)(lVar2 + 0x18);
    if (pcVar3 == (code *)0x0) {
      uVar1 = 0xffffff7a;
    }
    else {
      (*pcVar3)();
      uVar1 = 0;
    }
  }
  else {
    uVar1 = 0xffffffea;
  }
  return uVar1;
}


