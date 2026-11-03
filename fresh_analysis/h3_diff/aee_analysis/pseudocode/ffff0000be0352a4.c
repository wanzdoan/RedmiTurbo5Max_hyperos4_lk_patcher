
void FUN_ffff0000be0352a4(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  
  if (DAT_ffff0000be121818 != (undefined8 *)0x0) {
    FUN_ffff0000be060ae0(*DAT_ffff0000be121818,0,*(undefined4 *)(DAT_ffff0000be121818 + 1));
    if (DAT_ffff0000be121818 == (undefined8 *)0x0) {
      uVar1 = 0;
      uVar2 = 0;
    }
    else {
      uVar1 = *(undefined4 *)((long)DAT_ffff0000be121818 + 0x14);
      uVar2 = *(undefined4 *)(DAT_ffff0000be121818 + 3);
    }
    FUN_ffff0000be047cb0(0,0,uVar1,uVar2);
    return;
  }
  return;
}


