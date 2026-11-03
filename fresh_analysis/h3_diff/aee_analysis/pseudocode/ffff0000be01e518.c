
bool FUN_ffff0000be01e518(void)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  uVar3 = FUN_ffff0000be01e138();
  uVar4 = FUN_ffff0000be060e5c(&DAT_ffff0000be0a6a89);
  iVar2 = FUN_ffff0000be060f54(uVar3,&DAT_ffff0000be0a6a89,uVar4);
  if (iVar2 == 0) {
    bVar1 = true;
  }
  else {
    uVar3 = FUN_ffff0000be01e138();
    uVar4 = FUN_ffff0000be060e5c(&DAT_ffff0000be0b863d);
    iVar2 = FUN_ffff0000be060f54(uVar3,&DAT_ffff0000be0b863d,uVar4);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


