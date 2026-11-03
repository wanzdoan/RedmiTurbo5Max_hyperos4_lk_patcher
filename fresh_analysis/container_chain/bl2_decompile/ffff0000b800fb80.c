
bool FUN_ffff0000b800fb80(void)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  uVar3 = FUN_ffff0000b800f6d8();
  uVar4 = FUN_ffff0000b804d3cc(&DAT_ffff0000b8087e79);
  iVar2 = FUN_ffff0000b804d4c4(uVar3,&DAT_ffff0000b8087e79,uVar4);
  if (iVar2 == 0) {
    bVar1 = true;
  }
  else {
    uVar3 = FUN_ffff0000b800f6d8();
    uVar4 = FUN_ffff0000b804d3cc(&DAT_ffff0000b8096003);
    iVar2 = FUN_ffff0000b804d4c4(uVar3,&DAT_ffff0000b8096003,uVar4);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


