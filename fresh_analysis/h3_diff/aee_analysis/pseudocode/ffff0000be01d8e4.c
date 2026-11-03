
undefined8 FUN_ffff0000be01d8e4(undefined8 param_1,uint param_2)

{
  undefined8 uVar1;
  long lVar2;
  code *pcVar3;
  
  if (((param_2 < 2) && (*(long *)(&DAT_ffff0000be117638 + (ulong)param_2 * 8) != 0)) &&
     (lVar2 = *(long *)(*(long *)(&DAT_ffff0000be117638 + (ulong)param_2 * 8) + 0x18), lVar2 != 0))
  {
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


