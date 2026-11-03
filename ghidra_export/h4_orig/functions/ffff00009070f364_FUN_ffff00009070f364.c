// ffff00009070f364  size=464  FUN_ffff00009070f364

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070f364(long param_1,int param_2)

{
  long lVar1;
  byte bVar2;
  int iVar3;
  ulong uVar4;
  
  if (param_1 == 0) {
    iVar3 = -100;
  }
  else {
    if (*_DAT_ffff00009083e098 != '\0') {
      bVar2 = FUN_ffff00009070f294(param_1,param_2);
      DAT_ffff00009083e090 = bVar2 & 1;
      if ((bVar2 & 1) == 0) {
        FUN_ffff00009070cbb8(&DAT_ffff00009083e060,*(undefined8 *)(_DAT_ffff00009083e098 + 0x18));
        uVar4 = *(ulong *)(_DAT_ffff00009083e098 + 0x10);
      }
      else {
        FUN_ffff00009070ee2c(&DAT_ffff00009083e030);
        uVar4 = FUN_ffff00009070f2b4(param_1);
      }
      if (*(ulong *)(*(long *)(_DAT_ffff00009083e098 + 0x18) + 8) < uVar4) {
        FUN_ffff00009077e11c(s_size_too_large__space_small__ffff0000907d8ca2);
        FUN_ffff00009077e11c
                  (s_image_length_0x_llx__partition_m_ffff0000907e74ba,uVar4,
                   *(undefined8 *)(*(long *)(_DAT_ffff00009083e098 + 0x18) + 8));
        return -0x65;
      }
      *_DAT_ffff00009083e098 = '\0';
    }
    if (DAT_ffff00009083e090 == 0) {
      if (param_2 == 0) {
        FUN_ffff00009070cbf4(&DAT_ffff00009083e060);
        iVar3 = _DAT_ffff00009083e060;
      }
      else {
        FUN_ffff00009070cc00(&DAT_ffff00009083e060,param_1,param_2);
        iVar3 = _DAT_ffff00009083e060;
      }
    }
    else if (param_2 == 0) {
      FUN_ffff00009070ee70(&DAT_ffff00009083e030);
      iVar3 = _DAT_ffff00009083e030;
    }
    else {
      uVar4 = (long)*(int *)(_DAT_ffff00009083e0a0 + 0xf8) ^ 1;
      FUN_ffff00009070ee7c(&DAT_ffff00009083e030,param_1,param_2);
      iVar3 = _DAT_ffff00009083e030;
      if (_DAT_ffff00009083e030 == 0) {
        *(undefined4 *)(_DAT_ffff00009083e0a0 + (long)(int)uVar4 * 0x78 + 0x18) = 0;
      }
      else if (_DAT_ffff00009083e030 == 100) {
        lVar1 = uVar4 * 0x78;
        FUN_ffff00009077eb20
                  (*(long *)(_DAT_ffff00009083e0a0 + lVar1 + 8) +
                   (ulong)(0x1000 - _DAT_ffff00009083e038),_DAT_ffff00009083e040);
        _DAT_ffff00009083e030 = 0;
        *(int *)(_DAT_ffff00009083e0a0 + lVar1 + 0x18) = _DAT_ffff00009083e038;
        iVar3 = 0;
      }
    }
  }
  return iVar3;
}


