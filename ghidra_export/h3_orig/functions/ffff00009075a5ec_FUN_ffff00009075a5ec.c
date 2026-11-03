// ffff00009075a5ec  size=140  FUN_ffff00009075a5ec

void FUN_ffff00009075a5ec(long param_1)

{
  long lVar1;
  long lVar2;
  undefined4 *puVar3;
  
  lVar2 = 0;
  do {
    puVar3 = (undefined4 *)0xffff00009081ee00;
    if ((*(int *)(param_1 + 0x264) == 1) &&
       (puVar3 = (undefined4 *)0xffff00009081f058, *(int *)(param_1 + 0x738) != 0)) {
      puVar3 = &DAT_ffff00009081ef2c;
    }
    lVar1 = lVar2 + 4;
    *(undefined4 *)((long)&DAT_ffff00009081f3dc + lVar2) = *(undefined4 *)((long)puVar3 + lVar2);
    *(undefined4 *)((long)&DAT_ffff0000908208f4 + lVar2) =
         *(undefined4 *)((long)&DAT_ffff00009081f184 + lVar2);
    *(undefined4 *)((long)&DAT_ffff000090820a20 + lVar2) =
         *(undefined4 *)((long)&DAT_ffff00009081f2b0 + lVar2);
    lVar2 = lVar1;
  } while (lVar1 != 300);
  return;
}


