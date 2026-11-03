
void FUN_ffff0000b804310c(void)

{
  ulong uVar1;
  ulong uVar2;
  undefined4 local_24;
  
  local_24 = 0x27bc86aa;
  uVar2 = 0xfffffffffffffffc;
  do {
    uVar1 = uVar2 + 4;
    FUN_ffff0000b804cd40(&DAT_ffff000000114004 + uVar2,&local_24,4);
    uVar2 = uVar1;
  } while (uVar1 < 0x3fc);
  FUN_ffff0000b80075ac(0xffff000000114280,0x60);
  return;
}


