
void FUN_ffff00009075a884(long param_1)

{
  long lVar1;
  long lVar2;
  undefined4 *puVar3;
  
  lVar2 = 0;
  do {
    puVar3 = (undefined4 *)0xffff00009081ee20;
    if ((*(int *)(param_1 + 0x264) == 1) &&
       (puVar3 = (undefined4 *)0xffff00009081f078, *(int *)(param_1 + 0x738) != 0)) {
      puVar3 = &DAT_ffff00009081ef4c;
    }
    lVar1 = lVar2 + 4;
    *(undefined4 *)((long)&DAT_ffff00009081f3fc + lVar2) = *(undefined4 *)((long)puVar3 + lVar2);
    *(undefined4 *)((long)&DAT_ffff000090820914 + lVar2) =
         *(undefined4 *)((long)&DAT_ffff00009081f1a4 + lVar2);
    *(undefined4 *)((long)&DAT_ffff000090820a40 + lVar2) =
         *(undefined4 *)((long)&DAT_ffff00009081f2d0 + lVar2);
    lVar2 = lVar1;
  } while (lVar1 != 300);
  return;
}


