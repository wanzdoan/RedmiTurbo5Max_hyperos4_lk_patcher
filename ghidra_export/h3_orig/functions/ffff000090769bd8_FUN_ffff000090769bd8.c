// ffff000090769bd8  size=420  FUN_ffff000090769bd8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090769bd8(void)

{
  int iVar1;
  undefined *puVar2;
  long lVar3;
  undefined4 local_58;
  undefined2 local_54;
  
  FUN_ffff00009077de54(s__LK_LCM__s_ffff0000907fa5c5,s_lcm_init_ffff0000907e6a32);
  local_54 = 0;
  local_58 = 0x23700;
  (*_DAT_ffff00009087cdc8)(&local_58,1,1);
  (*_DAT_ffff00009087cdf8)(0xda,&local_54,1);
  FUN_ffff00009077de54(s__LK_LCM_0x_02hhx_ffff0000907d36c7,(undefined1)local_54);
  FUN_ffff00009077de54(s__LK_LCM_0x_02hhx_ffff0000907d36c7,local_54._1_1_);
  (*_DAT_ffff00009087cd90)(5);
  FUN_ffff000090785468(0x5a,0);
  FUN_ffff000090785354(0x5a,1);
  FUN_ffff0000907853c4(0x5a,1);
  (*_DAT_ffff00009087cd90)(10);
  FUN_ffff0000907853c4(0x5a,0);
  (*_DAT_ffff00009087cd90)(1);
  FUN_ffff0000907853c4(0x5a,1);
  (*_DAT_ffff00009087cd90)(0xb);
  lVar3 = 0xa6;
  puVar2 = &DAT_ffff00009082940d;
  do {
    iVar1 = *(int *)(puVar2 + -5);
    if (iVar1 == 0xfffb) {
      (*_DAT_ffff00009087cd88)(puVar2[-1]);
    }
    else if (iVar1 != 0xfffd) {
      if (iVar1 == 0xfffc) {
        (*_DAT_ffff00009087cd90)(puVar2[-1]);
      }
      else {
        (*_DAT_ffff00009087cdc0)(iVar1,puVar2[-1],puVar2,1);
      }
    }
    puVar2 = puVar2 + 0xd0;
    lVar3 = lVar3 + -1;
  } while (lVar3 != 0);
  return;
}


