
/* std::string android::base::Trim<std::string&>(std::string&) */

void __thiscall android::base::Trim<std::string&>(base *this,string *param_1)

{
  base *pbVar1;
  code *pcVar2;
  bool bVar3;
  long *in_x8;
  undefined1 *__dest;
  long lVar4;
  long lVar5;
  base *__src;
  ulong uVar6;

  uVar6 = *(ulong *)(this + 8);
  pbVar1 = *(base **)(this + 0x10);
  if (((byte)*this & 1) == 0) {
    pbVar1 = this + 1;
    uVar6 = (ulong)((byte)*this >> 1);
  }
  if (uVar6 != 0) {
    lVar4 = -uVar6;
    __src = pbVar1;
    do {
      if ((byte)*__src != 0x20 && (byte)*__src - 0xe < 0xfffffffb) {
        lVar5 = 0;
        goto LAB_00226a60;
      }
      __src = __src + 1;
      bVar3 = lVar4 != -1;
      lVar4 = lVar4 + 1;
    } while (bVar3);
  }
  goto LAB_00226a84;
  while (bVar3 = lVar4 + 1 != lVar5, lVar5 = lVar5 + -1, bVar3) {
LAB_00226a60:
    if ((byte)pbVar1[lVar5 + (uVar6 - 1)] != 0x20 &&
        (byte)pbVar1[lVar5 + (uVar6 - 1)] - 0xe < 0xfffffffb) {
      uVar6 = lVar5 - lVar4;
      if (0xffffffffffffffef < uVar6) {
        std::string::__throw_length_error_abi_ne180000_();
                    /* WARNING: Does not return */
        pcVar2 = (code *)UndefinedInstructionException(0,0x226b04);
        (*pcVar2)();
      }
      if (uVar6 < 0x17) {
        __dest = (undefined1 *)((long)in_x8 + 1);
        *(char *)in_x8 = (char)((int)uVar6 << 1);
      }
      else {
        __dest = operator_new((uVar6 | 0xf) + 1);
        in_x8[1] = uVar6;
        in_x8[2] = (long)__dest;
        *in_x8 = (uVar6 | 0xf) + 2;
      }
      memmove(__dest,__src,uVar6);
      goto LAB_00226a8c;
    }
  }
LAB_00226a84:
  uVar6 = 0;
  __dest = (undefined1 *)((long)in_x8 + 1);
  *(undefined1 *)in_x8 = 0;
LAB_00226a8c:
  __dest[uVar6] = 0;
  return;
}
