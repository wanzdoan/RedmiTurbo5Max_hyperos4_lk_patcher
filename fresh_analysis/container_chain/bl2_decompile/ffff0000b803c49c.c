
void FUN_ffff0000b803c49c(long param_1)

{
  long lVar1;
  long lVar2;
  undefined4 *puVar3;
  
  lVar2 = 0;
  do {
    puVar3 = (undefined4 *)0xffff0000b80ee670;
    if ((*(int *)(param_1 + 0x264) == 1) &&
       (puVar3 = (undefined4 *)0xffff0000b80ee8c8, *(int *)(param_1 + 0x738) != 0)) {
      puVar3 = &DAT_ffff0000b80ee79c;
    }
    lVar1 = lVar2 + 4;
    *(undefined4 *)((long)&DAT_ffff0000b80eec4c + lVar2) = *(undefined4 *)((long)puVar3 + lVar2);
    *(undefined4 *)((long)&DAT_ffff0000b80f0164 + lVar2) =
         *(undefined4 *)((long)&DAT_ffff0000b80ee9f4 + lVar2);
    *(undefined4 *)((long)&DAT_ffff0000b80f0290 + lVar2) =
         *(undefined4 *)((long)&DAT_ffff0000b80eeb20 + lVar2);
    lVar2 = lVar1;
  } while (lVar1 != 300);
  return;
}


