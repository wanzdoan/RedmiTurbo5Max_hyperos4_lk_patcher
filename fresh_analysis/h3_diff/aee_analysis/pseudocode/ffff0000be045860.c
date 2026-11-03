
void FUN_ffff0000be045860(long param_1)

{
  long lVar1;
  long lVar2;
  undefined4 *puVar3;
  
  lVar2 = 0;
  do {
    puVar3 = (undefined4 *)0xffff0000be0db740;
    if ((*(int *)(param_1 + 0x264) == 1) &&
       (puVar3 = (undefined4 *)0xffff0000be0db998, *(int *)(param_1 + 0x738) != 0)) {
      puVar3 = &DAT_ffff0000be0db86c;
    }
    lVar1 = lVar2 + 4;
    *(undefined4 *)((long)&DAT_ffff0000be0dbd1c + lVar2) = *(undefined4 *)((long)puVar3 + lVar2);
    *(undefined4 *)((long)&DAT_ffff0000be0dd234 + lVar2) =
         *(undefined4 *)((long)&DAT_ffff0000be0dbac4 + lVar2);
    *(undefined4 *)((long)&DAT_ffff0000be0dd360 + lVar2) =
         *(undefined4 *)((long)&DAT_ffff0000be0dbbf0 + lVar2);
    lVar2 = lVar1;
  } while (lVar1 != 300);
  return;
}


