
/* std::vector<std::string, std::allocator<std::string > >::insert(std::__wrap_iter<std::string
   const*>, std::string&&) */

byte * __thiscall
std::vector<std::string,std::allocator<std::string>>::insert
          (vector<std::string,std::allocator<std::string>> *this,byte *param_2,string *param_3)

{
  long lVar1;
  vector<std::string,std::allocator<std::string>> *pvVar2;
  byte *pbVar3;
  undefined8 uVar4;
  byte *pbVar5;
  ulong uVar6;
  ulong uVar7;
  byte *pbVar8;
  long lVar9;
  byte *pbVar10;
  long lVar11;
  undefined8 uVar12;
  byte *local_80;
  byte *local_78;
  byte *local_70;
  void *local_68;
  vector<std::string,std::allocator<std::string>> *local_60;
  long local_58;

  lVar1 = tpidr_el0;
  local_58 = *(long *)(lVar1 + 0x28);
  pbVar10 = *(byte **)(this + 8);
  pvVar2 = this + 0x10;
  if (pbVar10 < *(byte **)pvVar2) {
    pbVar8 = param_2;
    if ((long)param_2 - (long)pbVar10 == 0) {
      uVar12 = *(undefined8 *)(param_3 + 8);
      uVar4 = *(undefined8 *)param_3;
      *(undefined8 *)(param_2 + 0x10) = *(undefined8 *)(param_3 + 0x10);
      *(undefined8 *)(param_2 + 8) = uVar12;
      *(undefined8 *)param_2 = uVar4;
      *(undefined8 *)(param_3 + 8) = 0;
      *(undefined8 *)(param_3 + 0x10) = 0;
      *(undefined8 *)param_3 = 0;
      *(byte **)(this + 8) = param_2 + 0x18;
    }
    else {
      pbVar5 = pbVar10 + -0x18;
      pbVar3 = pbVar10;
      if (pbVar5 < pbVar10) {
        *(undefined8 *)(pbVar10 + 0x10) = *(undefined8 *)(pbVar10 + -8);
        pbVar3 = pbVar10 + 0x18;
        *(undefined8 *)(pbVar10 + 8) = *(undefined8 *)(pbVar10 + -0x10);
        *(undefined8 *)pbVar10 = *(undefined8 *)pbVar5;
        pbVar10[-0x10] = 0;
        pbVar10[-0xf] = 0;
        pbVar10[-0xe] = 0;
        pbVar10[-0xd] = 0;
        pbVar10[-0xc] = 0;
        pbVar10[-0xb] = 0;
        pbVar10[-10] = 0;
        pbVar10[-9] = 0;
        pbVar10[-8] = 0;
        pbVar10[-7] = 0;
        pbVar10[-6] = 0;
        pbVar10[-5] = 0;
        pbVar10[-4] = 0;
        pbVar10[-3] = 0;
        pbVar10[-2] = 0;
        pbVar10[-1] = 0;
        pbVar5[0] = 0;
        pbVar5[1] = 0;
        pbVar5[2] = 0;
        pbVar5[3] = 0;
        pbVar5[4] = 0;
        pbVar5[5] = 0;
        pbVar5[6] = 0;
        pbVar5[7] = 0;
      }
      *(byte **)(this + 8) = pbVar3;
      if (pbVar10 != param_2 + 0x18) {
        lVar11 = 0;
        do {
          if ((pbVar10[lVar11 + -0x18] & 1) != 0) {
            operator_delete(*(void **)(pbVar10 + lVar11 + -8));
          }
          lVar9 = lVar11 + -0x18;
          *(undefined8 *)(pbVar10 + lVar11 + -8) = *(undefined8 *)(pbVar10 + lVar11 + -0x20);
          *(undefined8 *)(pbVar10 + lVar11 + -0x10) = *(undefined8 *)(pbVar10 + lVar11 + -0x28);
          *(undefined8 *)(pbVar10 + lVar11 + -0x18) = *(undefined8 *)(pbVar10 + lVar11 + -0x30);
          pbVar10[lVar11 + -0x30] = 0;
          pbVar10[lVar11 + -0x2f] = 0;
          lVar11 = lVar9;
        } while (((long)param_2 - (long)pbVar10) + 0x18 != lVar9);
      }
      if ((*param_2 & 1) != 0) {
        operator_delete(*(void **)(param_2 + 0x10));
      }
      uVar12 = *(undefined8 *)(param_3 + 8);
      uVar4 = *(undefined8 *)param_3;
      *(undefined8 *)(param_2 + 0x10) = *(undefined8 *)(param_3 + 0x10);
      *(undefined8 *)(param_2 + 8) = uVar12;
      *(undefined8 *)param_2 = uVar4;
      *(undefined2 *)param_3 = 0;
    }
  }
  else {
    lVar11 = *(long *)this;
    uVar6 = ((long)pbVar10 - lVar11 >> 3) * -0x5555555555555555 + 1;
    if (0xaaaaaaaaaaaaaaa < uVar6) {
      if (*(long *)(lVar1 + 0x28) != local_58) goto LAB_0022cf28;
      vector<unsigned_short,std::allocator<unsigned_short>>::__throw_length_error_abi_ne180000_();
LAB_0022cef0:
      if (*(long *)(lVar1 + 0x28) == local_58) {
                    /* WARNING: Subroutine does not return */
        __throw_bad_array_new_length_abi_ne180000_();
      }
      goto LAB_0022cf28;
    }
    lVar9 = (long)*(byte **)pvVar2 - lVar11 >> 3;
    uVar7 = lVar9 * 0x5555555555555556;
    if (uVar7 < uVar6 || uVar7 - uVar6 == 0) {
      uVar7 = uVar6;
    }
    if (0x555555555555554 < (ulong)(lVar9 * -0x5555555555555555)) {
      uVar7 = 0xaaaaaaaaaaaaaaa;
    }
    local_60 = pvVar2;
    if (uVar7 == 0) {
      local_80 = (void *)0x0;
    }
    else {
      if (0xaaaaaaaaaaaaaaa < uVar7) goto LAB_0022cef0;
      local_80 = operator_new(uVar7 * 0x18);
    }
    local_78 = (byte *)((long)local_80 + ((long)param_2 - lVar11 >> 3) * 8);
    local_68 = (void *)((long)local_80 + uVar7 * 0x18);
    local_70 = local_78;
    __split_buffer<std::string,std::allocator<std::string>&>::push_back
              ((__split_buffer<std::string,std::allocator<std::string>&> *)&local_80,param_3);
    pbVar8 = local_78;
    pbVar3 = *(byte **)this;
    for (pbVar10 = param_2; pbVar3 != pbVar10; pbVar10 = pbVar10 + -0x18) {
      uVar12 = *(undefined8 *)(pbVar10 + -0x10);
      uVar4 = *(undefined8 *)(pbVar10 + -0x18);
      *(undefined8 *)(local_78 + -8) = *(undefined8 *)(pbVar10 + -8);
      *(undefined8 *)(local_78 + -0x10) = uVar12;
      *(undefined8 *)(local_78 + -0x18) = uVar4;
      pbVar10[-0x10] = 0;
      pbVar10[-0xf] = 0;
      pbVar10[-0xe] = 0;
      pbVar10[-0xd] = 0;
      pbVar10[-0xc] = 0;
      pbVar10[-0xb] = 0;
      pbVar10[-10] = 0;
      pbVar10[-9] = 0;
      pbVar10[-8] = 0;
      pbVar10[-7] = 0;
      pbVar10[-6] = 0;
      pbVar10[-5] = 0;
      pbVar10[-4] = 0;
      pbVar10[-3] = 0;
      pbVar10[-2] = 0;
      pbVar10[-1] = 0;
      pbVar10[-0x18] = 0;
      pbVar10[-0x17] = 0;
      pbVar10[-0x16] = 0;
      pbVar10[-0x15] = 0;
      pbVar10[-0x14] = 0;
      pbVar10[-0x13] = 0;
      pbVar10[-0x12] = 0;
      pbVar10[-0x11] = 0;
      local_78 = local_78 + -0x18;
    }
    pbVar3 = *(byte **)(this + 8);
    pbVar10 = local_70;
    if (pbVar3 != param_2) {
      do {
        uVar12 = *(undefined8 *)(param_2 + 8);
        uVar4 = *(undefined8 *)param_2;
        *(undefined8 *)(pbVar10 + 0x10) = *(undefined8 *)(param_2 + 0x10);
        local_70 = pbVar10 + 0x18;
        *(undefined8 *)(pbVar10 + 8) = uVar12;
        *(undefined8 *)pbVar10 = uVar4;
        param_2[8] = 0;
        param_2[9] = 0;
        param_2[10] = 0;
        param_2[0xb] = 0;
        param_2[0xc] = 0;
        param_2[0xd] = 0;
        param_2[0xe] = 0;
        param_2[0xf] = 0;
        param_2[0x10] = 0;
        param_2[0x11] = 0;
        param_2[0x12] = 0;
        param_2[0x13] = 0;
        param_2[0x14] = 0;
        param_2[0x15] = 0;
        param_2[0x16] = 0;
        param_2[0x17] = 0;
        pbVar5 = param_2 + 0x18;
        param_2[0] = 0;
        param_2[1] = 0;
        param_2[2] = 0;
        param_2[3] = 0;
        param_2[4] = 0;
        param_2[5] = 0;
        param_2[6] = 0;
        param_2[7] = 0;
        pbVar10 = local_70;
        param_2 = pbVar5;
      } while (pbVar5 != pbVar3);
      param_2 = *(byte **)(this + 8);
    }
    pbVar3 = *(byte **)this;
    *(byte **)this = local_78;
    *(byte **)(this + 8) = local_70;
    uVar4 = *(undefined8 *)(this + 0x10);
    *(void **)(this + 0x10) = local_68;
    local_80 = pbVar3;
    local_78 = pbVar3;
    local_68 = (void *)uVar4;
    pbVar10 = param_2;
    while (pbVar5 = pbVar10, pbVar5 != pbVar3) {
      local_70 = pbVar5 + -0x18;
      param_2 = local_80;
      pbVar10 = local_70;
      if ((pbVar5[-0x18] & 1) != 0) {
        operator_delete(*(void **)(pbVar5 + -8));
        param_2 = local_80;
        pbVar10 = local_70;
      }
    }
    local_70 = pbVar5;
    if (param_2 != (byte *)0x0) {
      operator_delete(param_2);
    }
  }
  if (*(long *)(lVar1 + 0x28) == local_58) {
    return pbVar8;
  }
LAB_0022cf28:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
