
/* std::string* std::vector<std::string, std::allocator<std::string >
   >::__push_back_slow_path<std::string >(std::string&&) */

string * __thiscall
std::vector<std::string,std::allocator<std::string>>::__push_back_slow_path<std::string>
          (vector<std::string,std::allocator<std::string>> *this,string *param_1)

{
  ulong uVar1;
  string *psVar2;
  byte *pbVar3;
  byte *pbVar4;
  void *pvVar5;
  byte *pbVar6;
  long lVar7;
  undefined8 *puVar8;
  long lVar9;
  ulong uVar10;
  byte *pbVar11;
  undefined8 uVar12;
  undefined8 uVar13;
  undefined8 uVar14;

  lVar7 = *(long *)(this + 8) - *(long *)this >> 3;
  uVar1 = lVar7 * -0x5555555555555555 + 1;
  if (0xaaaaaaaaaaaaaaa < uVar1) {
    vector<unsigned_short,std::allocator<unsigned_short>>::__throw_length_error_abi_ne180000_();
LAB_00226048:
                    /* WARNING: Subroutine does not return */
    __throw_bad_array_new_length_abi_ne180000_();
  }
  lVar9 = *(long *)(this + 0x10) - *(long *)this >> 3;
  uVar10 = lVar9 * 0x5555555555555556;
  if (uVar10 < uVar1 || uVar10 - uVar1 == 0) {
    uVar10 = uVar1;
  }
  if (0x555555555555554 < (ulong)(lVar9 * -0x5555555555555555)) {
    uVar10 = 0xaaaaaaaaaaaaaaa;
  }
  if (uVar10 == 0) {
    pvVar5 = (void *)0x0;
  }
  else {
    if (0xaaaaaaaaaaaaaaa < uVar10) goto LAB_00226048;
    pvVar5 = operator_new(uVar10 * 0x18);
  }
  uVar12 = *(undefined8 *)(param_1 + 0x10);
  uVar14 = *(undefined8 *)(param_1 + 8);
  uVar13 = *(undefined8 *)param_1;
  *(undefined8 *)param_1 = 0;
  *(undefined8 *)(param_1 + 8) = 0;
  *(undefined8 *)(param_1 + 0x10) = 0;
  puVar8 = (undefined8 *)((long)pvVar5 + lVar7 * 8);
  pbVar3 = *(byte **)this;
  pbVar6 = *(byte **)(this + 8);
  pvVar5 = (void *)((long)pvVar5 + uVar10 * 0x18);
  psVar2 = (string *)(puVar8 + 3);
  puVar8[1] = uVar14;
  *puVar8 = uVar13;
  puVar8[2] = uVar12;
  if (pbVar6 == pbVar3) {
    *(undefined8 **)this = puVar8;
    *(string **)(this + 8) = psVar2;
    *(void **)(this + 0x10) = pvVar5;
  }
  else {
    do {
      uVar13 = *(undefined8 *)(pbVar6 + -0x10);
      uVar12 = *(undefined8 *)(pbVar6 + -0x18);
      puVar8[-1] = *(undefined8 *)(pbVar6 + -8);
      pbVar11 = pbVar6 + -0x18;
      puVar8[-2] = uVar13;
      puVar8[-3] = uVar12;
      puVar8 = puVar8 + -3;
      pbVar6[-0x10] = 0;
      pbVar6[-0xf] = 0;
      pbVar6[-0xe] = 0;
      pbVar6[-0xd] = 0;
      pbVar6[-0xc] = 0;
      pbVar6[-0xb] = 0;
      pbVar6[-10] = 0;
      pbVar6[-9] = 0;
      pbVar6[-8] = 0;
      pbVar6[-7] = 0;
      pbVar6[-6] = 0;
      pbVar6[-5] = 0;
      pbVar6[-4] = 0;
      pbVar6[-3] = 0;
      pbVar6[-2] = 0;
      pbVar6[-1] = 0;
      pbVar6[-0x18] = 0;
      pbVar6[-0x17] = 0;
      pbVar6[-0x16] = 0;
      pbVar6[-0x15] = 0;
      pbVar6[-0x14] = 0;
      pbVar6[-0x13] = 0;
      pbVar6[-0x12] = 0;
      pbVar6[-0x11] = 0;
      pbVar6 = pbVar11;
    } while (pbVar11 != pbVar3);
    pbVar3 = *(byte **)this;
    pbVar6 = *(byte **)(this + 8);
    *(undefined8 **)this = puVar8;
    *(string **)(this + 8) = psVar2;
    *(void **)(this + 0x10) = pvVar5;
    pbVar11 = pbVar6;
    while (pbVar4 = pbVar11, pbVar4 != pbVar3) {
      pbVar11 = pbVar4 + -0x18;
      pbVar6 = pbVar3;
      if ((*pbVar11 & 1) != 0) {
        operator_delete(*(void **)(pbVar4 + -8));
      }
    }
  }
  if (pbVar6 != (byte *)0x0) {
    operator_delete(pbVar6);
  }
  return psVar2;
}
