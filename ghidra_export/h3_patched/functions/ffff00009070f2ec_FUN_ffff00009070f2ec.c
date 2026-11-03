// ffff00009070f2ec  size=464  FUN_ffff00009070f2ec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070f2ec(long param_1,int param_2)

{
  long lVar1;
  byte bVar2;
  int iVar3;
  ulong uVar4;
  
  if (param_1 == 0) {
    iVar3 = -100;
  }
  else {
    if (*_DAT_ffff00009083e060 != '\0') {
      bVar2 = FUN_ffff00009070f21c(param_1,param_2);
      DAT_ffff00009083e058 = bVar2 & 1;
      if ((bVar2 & 1) == 0) {
        FUN_ffff00009070cb40(&DAT_ffff00009083e028,*(undefined8 *)(_DAT_ffff00009083e060 + 0x18));
        uVar4 = *(ulong *)(_DAT_ffff00009083e060 + 0x10);
      }
      else {
        FUN_ffff00009070edb4(&DAT_ffff00009083dff8);
        uVar4 = FUN_ffff00009070f23c(param_1);
      }
      if (*(ulong *)(*(long *)(_DAT_ffff00009083e060 + 0x18) + 8) < uVar4) {
        FUN_ffff00009077de54(s_size_too_large__space_small__ffff0000907d8c0b);
        FUN_ffff00009077de54
                  (s_image_length_0x_llx__partition_m_ffff0000907e735f,uVar4,
                   *(undefined8 *)(*(long *)(_DAT_ffff00009083e060 + 0x18) + 8));
        return -0x65;
      }
      *_DAT_ffff00009083e060 = '\0';
    }
    if (DAT_ffff00009083e058 == 0) {
      if (param_2 == 0) {
        FUN_ffff00009070cb7c(&DAT_ffff00009083e028);
        iVar3 = _DAT_ffff00009083e028;
      }
      else {
        FUN_ffff00009070cb88(&DAT_ffff00009083e028,param_1,param_2);
        iVar3 = _DAT_ffff00009083e028;
      }
    }
    else if (param_2 == 0) {
      FUN_ffff00009070edf8(&DAT_ffff00009083dff8);
      iVar3 = _DAT_ffff00009083dff8;
    }
    else {
      uVar4 = (long)*(int *)(_DAT_ffff00009083e068 + 0xf8) ^ 1;
      FUN_ffff00009070ee04(&DAT_ffff00009083dff8,param_1,param_2);
      iVar3 = _DAT_ffff00009083dff8;
      if (_DAT_ffff00009083dff8 == 0) {
        *(undefined4 *)(_DAT_ffff00009083e068 + (long)(int)uVar4 * 0x78 + 0x18) = 0;
      }
      else if (_DAT_ffff00009083dff8 == 100) {
        lVar1 = uVar4 * 0x78;
        FUN_ffff00009077e858
                  (*(long *)(_DAT_ffff00009083e068 + lVar1 + 8) +
                   (ulong)(0x1000 - _DAT_ffff00009083e000),_DAT_ffff00009083e008);
        _DAT_ffff00009083dff8 = 0;
        *(int *)(_DAT_ffff00009083e068 + lVar1 + 0x18) = _DAT_ffff00009083e000;
        iVar3 = 0;
      }
    }
  }
  return iVar3;
}


