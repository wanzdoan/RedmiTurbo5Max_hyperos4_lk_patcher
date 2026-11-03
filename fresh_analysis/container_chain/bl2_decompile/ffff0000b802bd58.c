
void FUN_ffff0000b802bd58(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  
  if (DAT_ffff0000b8125020 != (undefined8 *)0x0) {
    FUN_ffff0000b804d070(*DAT_ffff0000b8125020,0,*(undefined4 *)(DAT_ffff0000b8125020 + 1));
    if (DAT_ffff0000b8125020 == (undefined8 *)0x0) {
      uVar1 = 0;
      uVar2 = 0;
    }
    else {
      uVar1 = *(undefined4 *)((long)DAT_ffff0000b8125020 + 0x14);
      uVar2 = *(undefined4 *)(DAT_ffff0000b8125020 + 3);
    }
    FUN_ffff0000b803e8ec(0,0,uVar1,uVar2);
    return;
  }
  return;
}


